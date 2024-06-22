package com.blog.practiceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Getter
@ToString
public class PostCreate {

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;

    @Builder
    @Jacksonized
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
