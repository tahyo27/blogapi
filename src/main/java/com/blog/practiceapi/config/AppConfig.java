package com.blog.practiceapi.config;

import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.MemberRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.service.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    public AuthorizationService authorizationService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, StrDataConfig strDataConfig) {
        return new AuthorizationServiceImpl(memberRepository, passwordEncoder, strDataConfig);
    }
}
