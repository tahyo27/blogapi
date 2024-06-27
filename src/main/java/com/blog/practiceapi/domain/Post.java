package com.blog.practiceapi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comment> comments;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toPostEditor() {
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }
}
