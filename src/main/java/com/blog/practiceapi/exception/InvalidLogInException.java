package com.blog.practiceapi.exception;

public class InvalidLogInException extends BlogException{
    private static final String MESSAGE = "아이디/비밀번호가 일치하지 않습니다";

    public InvalidLogInException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
