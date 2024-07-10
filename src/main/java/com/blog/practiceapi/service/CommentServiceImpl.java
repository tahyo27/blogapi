package com.blog.practiceapi.service;


import com.blog.practiceapi.domain.Comment;
import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.repository.CommentRepository;
import com.blog.practiceapi.repository.PostRepository;
import com.blog.practiceapi.request.CreateComment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Override
    @Transactional
    public void write(Long postId, CreateComment commentRequest) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        Comment comment = Comment.builder()
                .post(post)
                .author(commentRequest.getAuthor())
                .password(commentRequest.getPassword())
                .content(commentRequest.getContent())
                .build();

        post.addComment(comment);

    }



    @Override
    @Transactional
    public void replyWrite(Long parentId, CreateComment commentRequest) {
        Comment comment = commentRepository.findById(parentId).orElseThrow(() -> new PostNotFound());

        Comment child = Comment.builder()
                .author(commentRequest.getAuthor())
                .password(commentRequest.getPassword())
                .content(commentRequest.getContent())
                .build();

        comment.addChild(child);
        commentRepository.save(child);
    }
}
