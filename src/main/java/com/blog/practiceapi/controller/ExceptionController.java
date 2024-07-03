package com.blog.practiceapi.controller;

import com.blog.practiceapi.exception.BlogException;
import com.blog.practiceapi.exception.NotBlankException;
import com.blog.practiceapi.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(MethodArgumentNotValidException e) {
        NotBlankException notBlankException = new NotBlankException(e);
        log.info("낫블랭크 익셉션 확인");
        return BlogException(notBlankException);
    }

    @ResponseBody
    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ErrorResponse> BlogException(BlogException e) {
        log.info("호출 확인입니다 exception called");
        int stCode = e.getStatusCode();

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(stCode))
                .msg(e.getMessage())
                .validationError(e.getValidationError())
                .build();

        log.info(">>>>>>에러리스폰스 테스트 = {}", errorResponse.toString());
        return ResponseEntity.status(stCode)
                .body(errorResponse);
    }
}
