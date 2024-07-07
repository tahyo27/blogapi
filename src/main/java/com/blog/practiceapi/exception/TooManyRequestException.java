package com.blog.practiceapi.exception;

public class TooManyRequestException extends BlogException{

    private static final String MESSAGE = "현재 요청이 많습니다 잠시 후 다시 시도하세요";
    public TooManyRequestException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 429;
    }
}
