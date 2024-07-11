package com.blog.practiceapi.exception;

public class PostNotFound extends BlogException {

    private static final String MESSAGE = "포스트를 찾을 수 없습니다";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
