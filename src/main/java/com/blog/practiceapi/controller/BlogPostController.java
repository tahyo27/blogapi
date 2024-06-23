package com.blog.practiceapi.controller;

import com.blog.practiceapi.request.PostCreate;
import com.blog.practiceapi.response.BlogPostResponse;
import com.blog.practiceapi.service.BlogPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogPostService;

    @PostMapping("/posts")
    public void writePost(@RequestBody @Valid PostCreate postRequest) throws Exception {
        blogPostService.write(postRequest);
    }
    @GetMapping("/posts/{blogPostId}")
    public BlogPostResponse getPost(@PathVariable(name = "blogPostId") Long blogPostId) {
        return blogPostService.get(blogPostId);
    }

    @GetMapping("/posts")
    public List<BlogPostResponse> getList(Pageable pageable) {

        return blogPostService.getList(pageable);
    }
}
