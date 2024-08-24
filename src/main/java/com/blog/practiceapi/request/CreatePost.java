package com.blog.practiceapi.request;

import com.blog.practiceapi.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Getter
@ToString
public class CreatePost {

    private static final String[] BAD_WORDS = {"시발", "개새끼", "씨발"};

    @NotBlank
    private final String title;

    @NotBlank
    private String content;

    @Builder
    @Jacksonized
    public CreatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void isValid() {
        String message = "비속어가 포함되어있습니다";
        for(String badWord : BAD_WORDS) {
            if(title.contains(badWord)) {
                throw new InvalidRequest(Map.of("title", message));
            } else if(content.contains(badWord)) {
                throw new InvalidRequest(Map.of("content", message));
            }
        }
    }

}
