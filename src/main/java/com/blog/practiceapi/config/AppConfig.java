package com.blog.practiceapi.config;

import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.service.CommentService;
import com.blog.practiceapi.service.CommentServiceImpl;
import com.blog.practiceapi.service.PostService;
import com.blog.practiceapi.service.PostServiceImpl;
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
    public PostService postService(PostRepository postRepository) {
        return new PostServiceImpl(postRepository);
    }

    @Bean
    public CommentService commentService(PostRepository postRepository, CommentRepository commentRepository) {
        return new CommentServiceImpl(postRepository, commentRepository);
    }
}
