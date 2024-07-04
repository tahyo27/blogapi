package com.blog.practiceapi.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JwtTempUser {
    private final String username;
    private final String password = "temp";
    private final String role;

    @Builder
    public JwtTempUser(String username, String role) {
        this.username = username;
        this.role = role;
    }
}
