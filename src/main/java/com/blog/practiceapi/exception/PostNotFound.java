package com.blog.practiceapi.exception;

public class PostNotFound extends RuntimeException{

    private static final String MESSAGE = "존재하지 않는 글";

    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(Throwable cause) {
        super(MESSAGE, cause);
    }
}
