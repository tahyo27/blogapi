package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Comment;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreateComment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("service comment 추가 테스트")
    @Transactional
    void service_comment_add_test() {
        //given
        Post post = Post.builder()
                .title("제목1")
                .content("내용1")
                .build();
        postRepository.save(post);

        Comment comment = Comment.builder()
                .post(post)
                .author("나입니다")
                .password("1234")
                .content("내용입니다")
                .build();

        post.addComment(comment);
        //when
        Post commentPost = postRepository.findById(post.getId()).orElseThrow(PostNotFound::new);

        //then
        Assertions.assertEquals(commentPost.getComments().get(0).getAuthor(), "나입니다");
        Assertions.assertEquals(commentPost.getComments().get(0).getPassword(), "1234");
        Assertions.assertEquals(commentPost.getComments().get(0).getContent(), "내용입니다");
    }
}