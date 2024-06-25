package com.blog.practiceapi.controller;

import com.blog.practiceapi.exception.PostNotFound;
import com.blog.practiceapi.response.ErrorResponse;
import com.blog.practiceapi.response.ValidationError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ExceptionController {


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException e) {
        log.info("ExceptionController called");
        Map<String, String> fieldErrors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(objectError -> {
           String fieldName = ((FieldError) objectError).getField();
           String msg = objectError.getDefaultMessage();
           fieldErrors.put(fieldName, msg);
        });

        ValidationError validationError = new ValidationError(fieldErrors);

        return ErrorResponse.builder()
                .code("400")
                .msg("validation error")
                .validationError(validationError)
                .build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFound.class)
    public ErrorResponse exceptionHandler(PostNotFound e) {
        log.info("PostNotFound exception called");

        return  ErrorResponse.builder()
                .code("404")
                .msg("PostNotFound")
                .build();
    }
}
