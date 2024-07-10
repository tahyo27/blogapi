package com.blog.practiceapi.service;

import com.blog.practiceapi.request.CreateComment;

public interface CommentService {
    void write(Long postId, CreateComment commentRequest);

    void replyWrite(Long parentId, CreateComment commentRequest);
}
