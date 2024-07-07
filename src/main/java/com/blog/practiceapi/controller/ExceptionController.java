package com.blog.practiceapi.controller;

import com.blog.practiceapi.exception.BlogException;
import com.blog.practiceapi.exception.NotBlankException;
import com.blog.practiceapi.exception.TooManyRequestException;
import com.blog.practiceapi.response.ErrorResponse;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(MethodArgumentNotValidException e) {
        return BlogException(new NotBlankException(e));
    }

    @ResponseBody
    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ErrorResponse> BlogException(BlogException e) {
        int stCode = e.getStatusCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(stCode))
                .msg(e.getMessage())
                .validationError(e.getValidationError())
                .build();

        return ResponseEntity.status(stCode)
                .body(errorResponse);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorResponse> requestLimitException(RequestNotPermitted e) {
        return BlogException(new TooManyRequestException());
    }
}
