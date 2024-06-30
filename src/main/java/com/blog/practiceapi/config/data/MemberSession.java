package com.blog.practiceapi.config.data;

import lombok.Getter;

@Getter
public class MemberSession {

    public Long memberId;

    public MemberSession(Long memberId) {
        this.memberId = memberId;
    }
}
