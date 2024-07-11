package com.blog.practiceapi.controller;


import com.blog.practiceapi.request.CreateComment;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.response.CommentResponse;
import com.blog.practiceapi.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public void write(@PathVariable(value = "postId") Long postId, @RequestBody @Valid CreateComment commentRequest) {
        commentService.write(postId, commentRequest);
    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentResponse> getCommentLists(@PathVariable(value = "postId") Long postId) {
        //        String json = new ObjectMapper().writeValueAsString(commentResponses);
//        log.info(">>>>>>>>>>>>>>>>>>>>>>> json = {}", json);
        return commentService.getList(postId);
    }
}
