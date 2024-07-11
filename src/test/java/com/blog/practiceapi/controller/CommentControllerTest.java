package com.blog.practiceapi.controller;

import com.blog.practiceapi.domain.Comment;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreateComment;
import com.blog.practiceapi.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@AutoConfigureMockMvc
@WithMockUser(username = "tahyo27@gmail.com", roles = {"ADMIN"})
@ActiveProfiles("test")
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    //
    @Test
    @DisplayName("댓글 테스트")
    void comment_controller_test() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);
        Long postId = post.getId();

        CreateComment grandfather = CreateComment.builder()
                .author("할아버지")
                .content("할아버지내용")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(grandfather);

        //when
        mockMvc.perform(post("/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        List<Comment> comments = commentRepository.findAll();
        log.info(">>>>>>>>>>>>>>>>> comments = {}", comments.get(0).getId());
        //then
        Assertions.assertEquals(comments.size(), 1L);

    }

    @Test
    @DisplayName("대댓글 테스트")
    void comment_reply_test() throws Exception {
        //given
        Map<String, Long> mp = testObj();

        Long postId = mp.get("post");
        Long parentId = mp.get("comment");
        
        CreateComment father = CreateComment.builder()
                .author("할아버지아들")
                .parentId(parentId)
                .content("할아버지내용")
                .password("1234")
                .build();

        String jsonFather = objectMapper.writeValueAsString(father);

        log.info(">>>>>>>>>>>>>>>>>>>>>>> {}", parentId);
        //when

        mockMvc.perform(post("/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonFather)
                )
                .andExpect(status().isOk())
                .andDo(print());

        List<Comment> comments = commentRepository.findAll();

        //then
        Assertions.assertEquals(comments.size(), 4L);

    }

    @Test
    @DisplayName("댓글 대댓글 계층 출력 테스트")
    void comment_reply_print_test() throws Exception {
        //given
        Long postId = testObj().get("post");

        //expected
        mockMvc.perform(get("/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].children[0].author", Matchers.is("아빠작성자")))
                .andDo(print());

    }

    @Transactional
    private Map<String, Long> testObj() {
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();

        Comment parentComment = Comment.builder()
                .post(post)
                .author("할아버지작성자")
                .password("할아버지비번")
                .content("할아버지내용")
                .build();

        post.addComment(parentComment);

        Comment childComment = Comment.builder()
                .post(post)
                .parent(parentComment)
                .author("아빠작성자")
                .password("아빠비번")
                .content("아빠내용")
                .build();

        post.addComment(childComment);

        Comment childChild = Comment.builder()
                .post(post)
                .parent(childComment)
                .author("아들작성자")
                .password("아들비번")
                .content("아들내용")
                .build();

        post.addComment(childChild);

        postRepository.save(post);

        return Map.of("post", post.getId(), "comment", parentComment.getId());
    }


}