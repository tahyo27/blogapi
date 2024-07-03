package com.blog.practiceapi.exception;

public class JwtNotFoundException extends BlogException{
    private static final String MESSAGE = "JWT 키 찾을 수 없음";

    public JwtNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
