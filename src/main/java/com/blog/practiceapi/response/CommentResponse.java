package com.blog.practiceapi.response;

import com.blog.practiceapi.domain.Comment;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class CommentResponse {

    private Long id;
    private String author;
    private String password;
    private String content;
    private Long parentId;
    private LocalDateTime regdate;
    private List<CommentResponse> children = new ArrayList<>(); //출력할때 자식 넣는 용도

    public CommentResponse(Long id, String author, String password, String content, Long parentId, LocalDateTime regdate) {
        this.id = id;
        this.author = author;
        this.password = password;
        this.content = content;
        this.parentId = parentId;
        this.regdate = regdate;
    }

    public static CommentResponse convertComment(Comment comment) { //todo 리팩토링 생각
        return new CommentResponse(comment.getId(), comment.getAuthor(), comment.getPassword()
        , comment.getContent(), (comment.getParent() == null ? null : comment.getParent().getId()), comment.getRegdate());
    }


}
