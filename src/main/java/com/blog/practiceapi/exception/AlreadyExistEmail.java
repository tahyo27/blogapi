package com.blog.practiceapi.exception;

public class AlreadyExistEmail extends BlogException{

    private static final String MESSAGE = "이미 존재하는 이메일";

    public AlreadyExistEmail() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
