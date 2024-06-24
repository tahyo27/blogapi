package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.repository.BlogPostRepository;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.blog.practiceapi.response.BlogPostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
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
    @DisplayName("Json Request 클래스 블로그 포스트 저장 테스트")
    void save_post_service_test() {
        //give
        CreatePost createPost = CreatePost.builder()
                .title("제목")
                .content("내용")
                .build();

        //when
        blogPostService.write(createPost);

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
        Post post = blogPostRepository.findById(testPost.getId()).orElseThrow(() -> new IllegalStateException());

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
        Post post = blogPostRepository.findById(testPost.getId()).orElseThrow(() -> new IllegalStateException());

        //then
        assertNotNull(post);
        assertThat(post.getTitle()).isEqualTo("제목");
        assertThat(post.getContent()).isEqualTo("내용");
    }

    @Test
    @DisplayName("페이징 페이지 조회")
    void get_post_paging_test() {
        //given
        List<Post> savePosts = IntStream.range(1, 31)
                        .mapToObj(items -> Post.builder()
                                .title("제목" + items)
                                .content("내용" + items)
                                .build()).toList();
       blogPostRepository.saveAll(savePosts);

        //when
        SearchPagingPost search = SearchPagingPost.builder()
                .build();
        List<BlogPostResponse> posts = blogPostService.getList(search);

        //then

        Assertions.assertEquals(10L, posts.size());
        Assertions.assertEquals("제목30", posts.get(0).getTitle());
        Assertions.assertEquals("제목21", posts.get(9).getTitle());
    }
}