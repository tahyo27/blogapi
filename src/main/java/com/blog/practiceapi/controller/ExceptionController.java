package com.blog.practiceapi.controller;

import com.blog.practiceapi.exception.BlogException;
import com.blog.practiceapi.exception.NotBlankException;
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
        NotBlankException notBlankException = new NotBlankException(e);
        log.info("낫블랭크 익셉션 확인");
        return BlogException(notBlankException);
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

        log.info(">>>>>>에러리스폰스 테스트 = {}", errorResponse.toString());
        return ResponseEntity.status(stCode)
                .body(errorResponse);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ErrorResponse> requestLimitException(RequestNotPermitted e) {
        HttpStatus stCode = HttpStatus.TOO_MANY_REQUESTS;
        String message = "현재 요청이 많습니다 잠시 후 다시 시도하세요";

        ErrorResponse errorResponse = ErrorResponse.builder() //todo webmvcconfig에서 예외처리 리졸버 설정해서 blogException으로 다 받을 수 있게 할지 고민
                .code(String.valueOf(stCode))
                .msg(message)
                .build();

        return ResponseEntity.status(stCode)
                .body(errorResponse);
    }
}
