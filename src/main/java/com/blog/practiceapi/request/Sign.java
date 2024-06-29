package com.blog.practiceapi.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Sign {

    private String name;
    private String email;
    private String password;

    @Builder
    public Sign(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
