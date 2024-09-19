package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    void deleteByPostIdAndImagePathIn(Long postId, List<String> imagePaths);

    @Query("SELECT i.imagePath FROM Image i WHERE i.post.id = :postId") //Image 할때 db가 아니라 엔티티 객체라 I를 대문자로 해야함
    List<String> findImagePathsByPostId(@Param("postId") Long postId);

}
