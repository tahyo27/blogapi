package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.request.PostCreate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private final BlogPostRepository blogPostRepository;
    @Override
    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        blogPostRepository.save(post);
    }

    @Override
    public Post get(Long id) {
        Post post = blogPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("글이 존재하지 않음"));
        return post;
    }
}
