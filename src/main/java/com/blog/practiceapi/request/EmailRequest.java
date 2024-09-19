package com.blog.practiceapi.request;

import lombok.Builder;
import lombok.Getter;

public class EmailRequest {
    private String name;
    private String title;
    private String message;

    @Builder
    public EmailRequest(String name, String title, String message) {
        this.name = name;
        this.title = title;
        this.message = message;
    }

    public String getMessage() {
        return "이름 : " + this.name + " 이름 : " + this.title + " 내용 : " + this.message;
    }
}
