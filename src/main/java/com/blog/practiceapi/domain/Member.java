package com.blog.practiceapi.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Session> sessionLists = new ArrayList<>();

    @Builder
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Session addSession() {
        Session session = Session.builder()
                .member(this)
                .build();
        sessionLists.add(session);

        return session;
    }
}
