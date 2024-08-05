package com.blog.practiceapi.controller;


import com.blog.practiceapi.domain.Comment;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreateComment;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(username = "tahyo27@gmail.com", roles = {"ADMIN"})
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.myblog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class CommentControllerRestDocsTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Rest Docs 댓글 조회")
    void rest_docs_get_comment_test() throws Exception {
        //given
        Map<String, Long> map = testObj();
        Long postId = map.get("post");


        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}/comments", postId)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-inquiry" , pathParameters(
                                parameterWithName("postId").description("포스트 ID")
                        ),
                        responseFields (
                                fieldWithPath("[].id").description("댓글 ID"),
                                fieldWithPath("[].parentId").description("부모 댓글"),
                                fieldWithPath("[].author").description("댓글 작성자"),
                                fieldWithPath("[].content").description("댓글 내용"),
                                fieldWithPath("[].regdate").description("작성 날짜"),
                                fieldWithPath("[].children").description("자식 댓글")
                        )
                ));
    }

    @Test
    @DisplayName("Rest Docs 댓글 등록")
    void rest_docs_write_comment_test() throws Exception {
        //given
        Map<String, Long> map = testObj();
        Long postId = map.get("post");
        Long parentId = map.get("comment");
        CreateComment comment = CreateComment.builder()
                .author("댓글작성자")
                .password("1234")
                .content("댓글내용")
                .parentId(parentId)
                .build();

        String json = objectMapper.writeValueAsString(comment);

        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/{postId}/comments", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("comment-regist" , requestFields(
                                fieldWithPath("author").description("댓글 제목"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("password").description("댓글 비밀번호"),
                                fieldWithPath("parentId").description("부모 댓글 ID")
                                )
                        )
                );
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
        postRepository.save(post);

        return Map.of("post", post.getId(), "comment", parentComment.getId());
    }


    //    @Transactional
//    private Long testObj() {
//        Post post = Post.builder()
//                .title("제목")
//                .content("내용")
//                .build();
//
//        Comment parentComment = Comment.builder()
//                .post(post)
//                .author("할아버지작성자")
//                .password("할아버지비번")
//                .content("할아버지내용")
//                .build();
//
//        post.addComment(parentComment);
//
//        Comment childComment = Comment.builder()
//                .post(post)
//                .parent(parentComment)
//                .author("아빠작성자")
//                .password("아빠비번")
//                .content("아빠내용")
//                .build();
//
//        post.addComment(childComment);
//
//        Comment childChild = Comment.builder()
//                .post(post)
//                .parent(childComment)
//                .author("아들작성자")
//                .password("아들비번")
//                .content("아들내용")
//                .build();
//
//        post.addComment(childChild);
//
//        postRepository.save(post);
//
//        return post.getId();
//    }

}
