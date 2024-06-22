package com.blog.practiceapi;

import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.service.BlogPostService;
import com.blog.practiceapi.service.BlogPostServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final BlogPostRepository blogPostRepository;

    @Bean
    public BlogPostService postService(BlogPostRepository blogPostRepository) {
        return new BlogPostServiceImpl(blogPostRepository);
    }
}
