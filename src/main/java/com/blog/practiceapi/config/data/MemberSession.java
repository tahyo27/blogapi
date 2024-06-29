package com.blog.practiceapi.config.data;

import lombok.Getter;

@Getter
public class MemberSession {

    public String name;

    public MemberSession(String name) {
        this.name = name;
    }
}
