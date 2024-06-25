package com.blog.practiceapi.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
public class EditPost {


    private final String title;
    private final String content;

    @Builder
    @Jacksonized
    public EditPost(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
