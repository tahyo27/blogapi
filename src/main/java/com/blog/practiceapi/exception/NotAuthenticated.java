package com.blog.practiceapi.exception;

public class NotAuthenticated extends BlogException{

    private static final String MESSAGE = "인증되지 않았습니다";

    public NotAuthenticated() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 401;
    }
}
