package com.blog.practiceapi.request;

import lombok.Builder;
import lombok.Getter;

public class EmailRequest {
    private String name;
    private String email;
    private String message;

    @Builder
    public EmailRequest(String name, String email, String message) {
        this.name = name;
        this.email = email;
        this.message = message;
    }

    public String getMessage() {
        return "이름 : " + this.name + " 메일 : " + this.email + " 내용 : " + this.message;
    }
}
