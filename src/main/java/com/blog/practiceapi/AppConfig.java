package com.blog.practiceapi;

import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.service.BlogPostService;
import com.blog.practiceapi.service.BlogPostServiceImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {

    @PersistenceContext
    private final EntityManager em;

    public AppConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public BlogPostService postService(BlogPostRepository blogPostRepository) {
        return new BlogPostServiceImpl(blogPostRepository);
    }
}
