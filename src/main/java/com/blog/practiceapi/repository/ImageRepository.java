package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByPostIdAndImagePathIn(Long postId, List<String> imagePaths);
}
