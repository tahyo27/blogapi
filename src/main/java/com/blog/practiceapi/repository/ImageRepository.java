package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByPostIdAndImagePathIn(Long postId, List<String> imagePaths);

    @Query("SELECT i.imagePath FROM image i WHERE i.post.id = :postId")
    List<String> findImagePathsByPostId(@Param("postId") Long postId);

}
