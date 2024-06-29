package com.blog.practiceapi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    private Member member;

    @Builder
    public Session(String token, Member member) {
        this.token = token;
        this.member = member;
    }
}
