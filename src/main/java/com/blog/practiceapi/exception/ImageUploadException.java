package com.blog.practiceapi.exception;

public class ImageUploadException extends BlogException{
    private static final String MESSAGE = "스토리지에 업로드를 실패했습니다.";

    public ImageUploadException() {
        super(MESSAGE);
    }
    @Override
    public int getStatusCode() {
        return 500;
    }
}
