package com.blog.practiceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;


@Getter
@ToString
public class Login {

    @NotBlank
    private String email; //이메일을 아이디로 사용

    @NotBlank
    private String password;

    @Builder
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
