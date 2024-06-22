package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.request.PostCreate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogPostServiceImplTest {

    @Autowired
    private BlogPostService blogPostService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @BeforeEach
    void clean() {
        blogPostRepository.deleteAll();
    }


    @Test
    @DisplayName("블로그 포스트 저장 테스트")
    void save_post_service_test() {
        //give
        PostCreate postCreate = PostCreate.builder()
                .title("제목")
                .content("내용")
                .build();

        //when
        blogPostService.write(postCreate);

        //then
        Assertions.assertEquals(1L, blogPostRepository.count());
        Post post = blogPostRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("블로그 포스트 1개 조회 테스트")
    void get_one_post_test() {
        //given
        Post testPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        blogPostRepository.save(testPost);

        //when
        Post post = blogPostService.get(testPost.getId());

        //then
        assertNotNull(post);
        assertEquals(1L, blogPostRepository.count());
    }

    @Test
    @DisplayName("블로그 포스트 1개 내용 테스트")
    void get_post_content_test() {
        //given
        Post testPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        blogPostRepository.save(testPost);

        //when
        Post post = blogPostService.get(testPost.getId());

        //then
        assertNotNull(post);
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }
}