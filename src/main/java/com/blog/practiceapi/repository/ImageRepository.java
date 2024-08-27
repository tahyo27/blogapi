package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
