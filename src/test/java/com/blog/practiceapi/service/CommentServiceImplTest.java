package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Comment;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreateComment;
import com.blog.practiceapi.request.CursorPaging;
import com.blog.practiceapi.response.CommentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
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


    @Test
    @Transactional
    @DisplayName("댓글 대댓글 저장 테스트")
    void comment_reply_test() {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        Comment parentComment = Comment.builder()
                .post(post)
                .author("할아버지작성자")
                .password("할아버지비번")
                .content("할아버지내용")
                .build();

        post.addComment(parentComment);

        Comment childComment = Comment.builder()
                .post(post)
                .author("아빠작성자")
                .password("아빠비번")
                .content("아빠내용")
                .build();

        parentComment.addChild(childComment);
        post.addComment(childComment);

        Comment childChild = Comment.builder()
                .post(post)
                .author("아들작성자")
                .password("아들비번")
                .content("아들내용")
                .build();
        childComment.addChild(childChild);
        post.addComment(childChild);

        //when
        List<Comment> comments = commentRepository.findCommentByPostId(post.getId());
        //then
        Assertions.assertEquals(comments.size(), 3);

    }

    @Test
    @Transactional
    @DisplayName("댓글 대댓글 리스폰스 객체 출력 테스트") //나중에 지움
    void comment_reply_response_test() throws JsonProcessingException {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        Comment parentComment = Comment.builder()
                .post(post)
                .author("할아버지작성자")
                .password("할아버지비번")
                .content("할아버지내용")
                .build();

        post.addComment(parentComment);

        Comment childComment = Comment.builder()
                .post(post)
                .author("아빠작성자")
                .password("아빠비번")
                .content("아빠내용")
                .build();

        parentComment.addChild(childComment);
        post.addComment(childComment);

        Comment childChild = Comment.builder()
                .post(post)
                .author("아들작성자")
                .password("아들비번")
                .content("아들내용")
                .build();
        childComment.addChild(childChild);
        post.addComment(childChild);

        //when
        List<Comment> comments = commentRepository.findCommentByPostId(post.getId());
        List<CommentResponse> responseList = new ArrayList<>();
        Map<Long, CommentResponse> map = new HashMap<>();
        comments.forEach(items -> {
            CommentResponse cr = CommentResponse.convert(items);
            map.put(cr.getId(), cr);
            if(items.getParent() != null) map.get(items.getParent().getId()).getChildren().add(cr);
            else responseList.add(cr);
        });

        responseList.forEach(items -> log.info(">>>>>>>>>> {}", items));
        String json = new ObjectMapper().writeValueAsString(responseList);
        log.info(">>>>>>>>>>>>>>>>>>>>>>> json = {}", json);
    }
}