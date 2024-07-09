package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Comment;

import java.util.List;

public interface CustomCommentRepository {
    List<Comment> findCommentByPostId(Long postId);
}
