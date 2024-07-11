package com.blog.practiceapi.service;

import com.blog.practiceapi.request.CreateComment;
import com.blog.practiceapi.response.CommentResponse;

import java.util.List;

public interface CommentService {
    void write(Long postId, CreateComment commentRequest);

    void replyWrite(Long parentId, CreateComment commentRequest);

    List<CommentResponse> getList(Long postId);
}
