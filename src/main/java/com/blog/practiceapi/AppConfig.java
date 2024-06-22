package com.blog.practiceapi;

import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.service.PostService;
import com.blog.practiceapi.service.PostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final PostRepository postRepository;

    @Bean
    public PostService postService(PostRepository postRepository) {
        return new PostServiceImpl(postRepository);
    }
}
