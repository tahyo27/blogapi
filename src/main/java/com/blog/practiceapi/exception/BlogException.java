package com.blog.practiceapi.exception;

import com.blog.practiceapi.response.ValidationError;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class BlogException extends RuntimeException{

    //private final Map<String, String> validation = new HashMap<>();

    private ValidationError validationError;

    public BlogException(String message) {
        super(message);
    }
    protected void addValidation(ValidationError validationError) {
        this.validationError = validationError;
    }

    public BlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();
}
