package com.blog.practiceapi.exception;

public class PostNotFound extends BlogException {

    private static final String MESSAGE = "존재하지 않는 글";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
