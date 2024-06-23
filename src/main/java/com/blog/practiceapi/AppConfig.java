package com.blog.practiceapi;

import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.service.BlogPostService;
import com.blog.practiceapi.service.BlogPostServiceImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @PersistenceContext
    private final EntityManager em;

    private final BlogPostRepository blogPostRepository;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
    @Bean
    public BlogPostService postService() {
        return new BlogPostServiceImpl(blogPostRepository);
    }
}
