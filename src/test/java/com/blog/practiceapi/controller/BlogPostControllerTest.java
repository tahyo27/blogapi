package com.blog.practiceapi.controller;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.request.PostCreate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
@AutoConfigureMockMvc
@SpringBootTest
class BlogPostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @BeforeEach
    void clean() {
        blogPostRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 출력 테스트")
    void controller_get_post_test() throws Exception {
        //geven
        PostCreate request = PostCreate.builder()
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
    void controller_post_save_db_test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(request);
        //when      post에 들어가는 내용
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
    @DisplayName("/posts 모두 불러오기 테스트")
    void controller_get_post_all_test() throws Exception {
        //given
        Post testPost1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        blogPostRepository.save(testPost1);

        Post testPost2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .build();
        blogPostRepository.save(testPost2);

        //expected

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id").value(testPost1.getId()))
                .andExpect(jsonPath("$[0].title").value("제목1"))
                .andExpect(jsonPath("$[0].content").value("내용1"))
                .andExpect(jsonPath("$[1].id").value(testPost2.getId()))
                .andExpect(jsonPath("$[1].title").value("제목2"))
                .andExpect(jsonPath("$[1].content").value("내용2"))
                .andDo(MockMvcResultHandlers.print());
    }
    
}//end