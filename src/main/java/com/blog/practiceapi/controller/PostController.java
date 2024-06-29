package com.blog.practiceapi.controller;

import com.blog.practiceapi.exception.InvalidRequest;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.EditPost;
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

    @GetMapping("/test")
    public String test() {

        return "test임";
    }
    @PostMapping("/posts")
    public void writePost(@RequestBody @Valid CreatePost postRequest, @RequestHeader String authorization) throws Exception {
        if(authorization.equals("psyduck")) {
            postRequest.isValid();
            postService.write(postRequest);
        }
    }
    @GetMapping("/posts/{postId}")
    public PostResponse getPost(@PathVariable(name = "postId") Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(SearchPagingPost searchPagingPost) {

        return postService.getList(searchPagingPost);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable(name = "postId") Long postId, @RequestBody EditPost editRequest) {
        postService.editPost(postId, editRequest);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable(name = "postId") Long postId) {
        postService.delete(postId);
    }
}
