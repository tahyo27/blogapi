package com.blog.practiceapi.controller;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.domain.PostEditor;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.EditPost;
import com.blog.practiceapi.service.PostService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@WithMockUser(username = "tahyo27@gmail.com", roles = {"ADMIN"})
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN id RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("/posts 요청시 출력 테스트")
    @Transactional
    void controller_get_post_test() throws Exception {
        //given
        CreatePost request = CreatePost.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(request);
        //expect
        mockMvc.perform(post("/posts")
                        .header("authorization", "psyduck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }
    
    @Test
    @DisplayName("/posts 요청시 title, content 공백 및 null 테스트")
    @Transactional
    void controller_get_exception_test() throws Exception {
        //expect
        mockMvc.perform(post("/posts")
                        .header("authorization", "psyduck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": null, \"content\": \"\"}")
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값 저장 인증")
    @Transactional
    void controller_post_save_db_test() throws Exception {
        //given
        CreatePost request = CreatePost.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(request);
        //when
        mockMvc.perform(post("/posts?authorization=psyduck")
                        .header("authorization", "psyduck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        //then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);

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

        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{blogPostId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("제목테스트"))
                .andExpect(jsonPath("$.content").value("내용테스트"))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 커서 페이징 테스트")
    @Transactional
    void controller_get_post_paging_test() throws Exception {
        //given
        List<Post> savePosts = IntStream.range(1, 31)
                .mapToObj(items -> Post.builder()
                        .title("제목" + items)
                        .content("내용" + items)
                        .build()).toList();
        postRepository.saveAll(savePosts);

        //expected

        mockMvc.perform(get("/posts")
                                .param("cursor", "")
                                .param("size", "10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목30"))
                .andExpect(jsonPath("$[0].content").value("내용30"))
                .andDo(print());
    }
    @Test
    @DisplayName("/posts 페이징 사이즈 제한 테스트")
    @Transactional
    void controller_get_post_paging_size_test() throws Exception {
        //given
        List<Post> savePosts = IntStream.range(1, 31)
                .mapToObj(items -> Post.builder()
                        .title("제목" + items)
                        .content("내용" + items)
                        .build()).toList();
        postRepository.saveAll(savePosts);

        //expected

        mockMvc.perform(get("/posts")
                        .param("cursor", "")
                        .param("size", "2000")
                        .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("제목30"))
                .andExpect(jsonPath("$[0].content").value("내용30"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 글 제목 업데이트 테스트")
    @Transactional
    void controller_edit_post_title_test() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        postRepository.save(post);

        EditPost editedPost = EditPost.builder()
                .title("목제1")
                .content(null)
                .build();

        PostEditor.PostEditorBuilder postEditorBuilder = post.toPostEditor();
        PostEditor postEditor= postEditorBuilder
                .title(editedPost.getTitle())
                .content(editedPost.getContent())
                .build();

        String json = (new ObjectMapper()).writeValueAsString(postEditor);
        //expected

        //생각해야할 부분 EditPost에서 null 허용 여부
        mockMvc.perform(patch("/posts/{postId}", post.getId()) //patch post/{postId}
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 없는글 삭제 테스트")
    @Transactional
    void controller_edit_post_fail_test() throws Exception {
        //given
        EditPost editedPost = EditPost.builder()
                .title("목제1")
                .content("용내1")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(editedPost);
        //expected

        mockMvc.perform(patch("/posts/{postId}", 2L) //patch post/{postId}
                        .header("authorization", "psyduck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("controller post 삭제")
    @Transactional
    void controller_post_delete_test() throws Exception {
        //given
        List<Post> posts = IntStream.range(1, 11)
                .mapToObj(items -> Post.builder()
                        .title("제목" + items)
                        .content("내용" + items)
                        .build()
                ).toList();

        postRepository.saveAll(posts);

        //when
        mockMvc.perform(delete("/posts/{postId}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());


        //then

        Assertions.assertEquals(9, postRepository.count());
    }

    @Test
    @DisplayName("controller 존재하지 않는 post 삭제")
    @Transactional
    void controller_post_delete_exception_test() throws Exception {
        //expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                                .header("authorization", "psyduck")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", Matchers.is("존재하지 않는 글")))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 저장시 제목 비속어 처리")
    @Transactional
    void controller_get_post_title_exception_test() throws Exception {
        //given
        Post post = Post.builder()
                .title("시발테스트")
                .content("내용테스트")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(post);
        //String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.mNHI7gEGjaLPNUmGmto3CBDBu_fz_KBQeVzWyJxKyuw";
        //expected
        mockMvc.perform(post("/posts")
                        .header("authorization", "psyduck")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }






}//end