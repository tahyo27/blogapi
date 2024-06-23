package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
}
