package com.blog.practiceapi.controller;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.service.BlogPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogPostControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @BeforeEach
    void clean() {
        blogPostRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("/posts 요청시 출력 테스트")
    @Transactional
    void controller_get_post_test() throws Exception {
        //geven
        CreatePost request = CreatePost.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(request);
        System.out.println(json);
        //expect
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    @DisplayName("/posts 요청시 title, content 공백 및 null 테스트")
    @Transactional
    void controller_get_exception_test() throws Exception {
        //expect
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": null, \"content\": \"\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("validation error"))
                .andExpect(jsonPath("$.validation.title").value("must not be blank"))
                .andExpect(jsonPath("$.validation.content").value("must not be blank"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값 저장")
    @Transactional
    void controller_post_save_db_test() throws Exception {
        //given
        CreatePost request = CreatePost.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(request);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        //then
        Assertions.assertEquals(1L, blogPostRepository.count());
        Post post = blogPostRepository.findAll().get(0);

        assertThat(post.getTitle()).isEqualTo("제목입니다");
        assertThat(post.getContent()).isEqualTo("내용입니다");
    }
    
    @Test
    @DisplayName("/posts/id 요청시 1개 조회 테스트")
    @Transactional
    void controller_get_post_with_id_test() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목테스트")
                .content("내용테스트")
                .build();

        blogPostRepository.save(post);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{blogPostId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("제목테스트"))
                .andExpect(jsonPath("$.content").value("내용테스트"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("/posts 페이징 테스트")
    @Transactional
    void controller_get_post_paging_test() throws Exception {
        //given
        List<Post> savePosts = IntStream.range(1, 31)
                .mapToObj(items -> Post.builder()
                        .title("제목" + items)
                        .content("내용" + items)
                        .build()).toList();
        blogPostRepository.saveAll(savePosts);

        //expected

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                                .param("page", "1")
                                .param("size", "10")
                                .param("sort", "id,desc")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목30"))
                .andExpect(jsonPath("$[0].content").value("내용30"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("/posts 페이지 기본값 테스트")
    @Transactional
    void controller_get_post_paging_default_test() throws Exception {
        //given
        List<Post> savePosts = IntStream.range(1, 31)
                .mapToObj(items -> Post.builder()
                        .title("제목" + items)
                        .content("내용" + items)
                        .build()).toList();
        blogPostRepository.saveAll(savePosts);

        //expected

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .param("page", "1")
                        .param("size", "2000")
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목30"))
                .andExpect(jsonPath("$[0].content").value("내용30"))
                .andDo(MockMvcResultHandlers.print());
    }
    
}//end