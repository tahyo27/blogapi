package com.blog.practiceapi.service;


import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreateComment;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public void write(Long postId, CreateComment commentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
    }
}
