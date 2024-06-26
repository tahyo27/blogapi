package com.blog.practiceapi.controller;


import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreatePost;
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
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.myblog.com", uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Rest Docs 1개 조회")
    void rest_docs_get_post_test() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();

        postRepository.save(post);

        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index" , pathParameters(
                        parameterWithName("postId").description("포스트 ID")
                        ),
                        responseFields (
                                fieldWithPath("id").description("포스트 ID"),
                                fieldWithPath("title").description("포스트 제목"),
                                fieldWithPath("content").description("포스트 내용")
                        )
                ));
    }

    @Test
    @DisplayName("Rest Docs 1개 등록")
    void rest_docs_write_post_test() throws Exception {
        //given
        CreatePost createPost = CreatePost.builder()
                .title("제목1")
                .content("내용1")
                .build();

        String json = (new ObjectMapper()).writeValueAsString(createPost);

        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("index" , requestFields(
                                fieldWithPath("title").description("포스트 제목"),
                                fieldWithPath("content").description("포스트 내용"))
                                )
                );
    }
}
