package com.blog.practiceapi.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BlogException extends RuntimeException{

    private final Map<String, String> validation = new HashMap<>();

    public BlogException(String message) {
        super(message);
    }
    protected void addValidation(String fieldName, String message) {
        validation.put(fieldName, message);
    }

    public BlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
