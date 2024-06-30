package com.blog.practiceapi.exception;

import com.blog.practiceapi.response.ValidationError;

import java.util.Map;

//정책상 400에러
public class InvalidRequest extends BlogException {

    private static final String MESSAGE = "잘못된 요청입니다";
    public InvalidRequest() {
        super(MESSAGE);
    }
    public InvalidRequest(Map<String, String> map) {
        super(MESSAGE);
        addValidation(new ValidationError(map));
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
