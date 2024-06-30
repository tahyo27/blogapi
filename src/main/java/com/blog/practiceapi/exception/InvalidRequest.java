package com.blog.practiceapi.exception;
//정책상 400에러
public class InvalidRequest extends BlogException {

    private static final String MESSAGE = "잘못된 요청입니다";
    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String msg) {
        super(MESSAGE);
        addValidation(fieldName, msg);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
