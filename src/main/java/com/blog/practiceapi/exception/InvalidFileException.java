package com.blog.practiceapi.exception;

public class InvalidFileException extends BlogException{
    private static final String MESSAGE = "유효하지 않은 파일";

    public InvalidFileException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 500;
    }
}
