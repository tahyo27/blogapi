package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.blog.practiceapi.response.PostResponse;
import com.blog.practiceapi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void writePost(@RequestBody @Valid CreatePost postRequest) throws Exception {
        postService.write(postRequest);
    }
    @GetMapping("/posts/{blogPostId}")
    public PostResponse getPost(@PathVariable(name = "blogPostId") Long blogPostId) {
        return postService.get(blogPostId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(SearchPagingPost searchPagingPost) {

        return postService.getList(searchPagingPost);
    }
}
