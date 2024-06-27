package com.blog.practiceapi.controller;


import com.blog.practiceapi.request.CreateComment;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable(value = "postId") Long postId, @RequestBody @Valid CreateComment commentRequest) {
        commentService.write(postId, commentRequest);
    }
}
