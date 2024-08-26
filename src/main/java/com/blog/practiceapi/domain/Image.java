package com.blog.practiceapi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originName;
    private String uniqueName;
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private  Post post;

    @Builder
    public Image(Long id, Post post, String originName, String uniqueName, String imagePath) {
        this.id = id;
        this.post = post;
        this.originName = originName;
        this.uniqueName = uniqueName;
        this.imagePath = imagePath;
    }

}
