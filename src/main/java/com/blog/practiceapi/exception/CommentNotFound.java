package com.blog.practiceapi.exception;

public class CommentNotFound extends BlogException{
    private static final String MESSAGE = "댓글을 찾을 수 없습니다";
    public CommentNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
