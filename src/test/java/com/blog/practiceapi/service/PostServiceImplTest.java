package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.EditPost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.blog.practiceapi.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostServiceImplTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
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
        postService.write(createPost);

        //then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
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
        postRepository.save(testPost);

        //when
        Post post = postRepository.findById(testPost.getId()).orElseThrow(IllegalStateException::new);

        //then
        assertNotNull(post);
        assertEquals(1L, postRepository.count());
    }

    @Test
    @DisplayName("블로그 포스트 1개 내용 테스트")
    void get_post_content_test() {
        //given
        Post testPost = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(testPost);

        //when
        Post post = postRepository.findById(testPost.getId()).orElseThrow(PostNotFound::new);

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
       postRepository.saveAll(savePosts);

        //when
        SearchPagingPost search = SearchPagingPost.builder()
                .build();
        List<PostResponse> posts = postService.getList(search);

        //then

        Assertions.assertEquals(10L, posts.size());
        Assertions.assertEquals("제목30", posts.get(0).getTitle());
        Assertions.assertEquals("제목21", posts.get(9).getTitle());
    }

    @Test
    @DisplayName("게시글 제목 수정")
    void post_edit_title_test() {
        //given
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        postRepository.save(post);

        EditPost editedPost = EditPost.builder()
                .title("목제1")
                .build();

        //when
        postService.editPost(post.getId(), editedPost);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않음" + post.getId()));

        Assertions.assertEquals(changedPost.getTitle(), editedPost.getTitle());
    }

    @Test
    @DisplayName("게시글 내용 수정")
    void post_edit_content_test() {
        //given
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        postRepository.save(post);

        EditPost editedPost = EditPost.builder()
                .title(null)
                .content("용내1")
                .build();

        //when
        postService.editPost(post.getId(), editedPost);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않음" + post.getId()));

        Assertions.assertEquals(changedPost.getContent(), editedPost.getContent());
    }

    @Test
    @DisplayName("post 삭제 테스트")
    void post_delete_test() {
        //given
        List<Post> posts = IntStream.range(1, 11)
                .mapToObj(items -> Post.builder()
                        .title("제목" + items)
                        .content("내용" + items)
                        .build()).toList();

        postRepository.saveAll(posts);

        //when
        postService.delete(posts.get(2).getId());


        //then
        Assertions.assertEquals(9, postRepository.count());
    }

    @Test
    @DisplayName("포스트 1개 조회 실패 테스트")
    void get_one_post_fail_test() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);
        //post.getId() // primary_id = 오류일떄

        //expected
        PostNotFound postNotFound = Assertions.assertThrows(PostNotFound.class, () ->
                postService.get(post.getId() + 1L));
    }

    @Test
    @DisplayName("존재 않는 포스트 삭제 ")
    void post_delete_exception_test() {
        //given

        List<Post> posts = IntStream.range(1, 11)
                .mapToObj(items -> Post.builder()
                        .title("제목" + items)
                        .content("내용" + items)
                        .build()).toList();

        postRepository.saveAll(posts);

        //expected
        Assertions.assertThrows(PostNotFound.class, () ->  postService.delete(12L));

    }





}