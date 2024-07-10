package com.blog.practiceapi.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author; // 비회원으로도 작성 가능

    @Column(nullable = false)
    private String password; // 비회원으로도 작성 가능

    @Column(nullable = false)
    private String content;

    private LocalDateTime regdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private  Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String author, String password, String content, Post post) {
        this.author = author;
        this.password = password;
        this.content = content;
        this.post = post;
        this.regdate = LocalDateTime.now();
    }

    public void addChild(Comment child) {
        child.parent = this;
        child.post = this.post;
        children.add(child);
    }
}
