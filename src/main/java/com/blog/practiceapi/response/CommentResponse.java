package com.blog.practiceapi.response;

import com.blog.practiceapi.domain.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class CommentResponse {

    private final Long id;
    private final String author;
    private final String content;
    private final Long parentId;
    private final String regdate;

    private final List<CommentResponse> children = new ArrayList<>(); //출력할때 자식 넣는 용도

    @Builder
    public CommentResponse(Long id, String author, String content, Long parentId, String regdate) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.parentId = parentId;
        this.regdate = regdate;
    }


    public static CommentResponse convert(Comment comment) {
        Long parentId = (comment.getParent()) == null ? null : comment.getParent().getId();
        String regdate = comment.getRegdate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return CommentResponse.builder()
                .parentId(parentId)
                .id(comment.getId())
                .author(comment.getAuthor())
                .regdate(regdate)
                .content(comment.getContent())
                .build();
    }


}
