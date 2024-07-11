package com.blog.practiceapi.service;


import com.blog.practiceapi.domain.Comment;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.exception.CommentNotFound;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreateComment;
import com.blog.practiceapi.response.CommentResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{


    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Override
    @Transactional
    public void write(Long postId, CreateComment commentRequest) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
        Comment parent = null;
        if(commentRequest.getParentId() != null) {
            parent = commentRepository.findById(commentRequest.getParentId()).orElseThrow(CommentNotFound::new);
        }
        Comment comment = Comment.builder()
                .post(post)
                .parent(parent)
                .author(commentRequest.getAuthor())
                .password(commentRequest.getPassword())
                .content(commentRequest.getContent())
                .build();

        post.addComment(comment);
    }



//    @Override
//    @Transactional
//    public void replyWrite(Long parentId, CreateComment commentRequest) { // todo 위랑 합칠지 말지 고민 어차피 포스트 확인 해야함
//        Comment comment = commentRepository.findById(parentId).orElseThrow(() -> new PostNotFound()); //todo 커멘트 익셉션 바꾸기
//
//        Comment child = Comment.builder()
//                .author(commentRequest.getAuthor())
//                .password(commentRequest.getPassword())
//                .content(commentRequest.getContent())
//                .build();
//
//        comment.addChild(child);
//        commentRepository.save(child);
//    }

    @Override
    public List<CommentResponse> getList(Long postId) {
        List<Comment> commentList = commentRepository.findCommentByPostId(postId);

        List<CommentResponse> responseList = new ArrayList<>();
        Map<Long, CommentResponse> map = new HashMap<>();
        commentList.forEach(items -> {
            CommentResponse cr = CommentResponse.convert(items);
            map.put(cr.getId(), cr);
            if(items.getParent() != null) map.get(items.getParent().getId()).getChildren().add(cr);
            else responseList.add(cr);
        });

        return responseList;
    }

//    @Override
//    @Transactional
//    public void writeTest(Long postId, CreateComment commentRequest) {
//        Post post = postRepository.findById(postId).orElseThrow(PostNotFound::new);
//        Comment parent = null;
//        if(commentRequest.getParentId() != null) {
//            parent = commentRepository.findById(commentRequest.getParentId()).orElseThrow(PostNotFound::new);
//        }
//        Comment comment = Comment.builder()
//                .post(post)
//                .parent(parent)
//                .author(commentRequest.getAuthor())
//                .password(commentRequest.getPassword())
//                .content(commentRequest.getContent())
//                .build();
//
//        post.addComment(comment);
//    }
}
