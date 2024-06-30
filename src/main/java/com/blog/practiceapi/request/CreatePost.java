package com.blog.practiceapi.request;

import com.blog.practiceapi.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@ToString
public class CreatePost {

    private static final String[] BAD_WORDS = {"시발", "개새끼", "씨발"};

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;

    @Builder
    @Jacksonized
    public CreatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void isValid() {
        for(String badWord : BAD_WORDS) {
            if(title.contains(badWord)) {
                throw new InvalidRequest(Map.of("title", "비속어가 포함되어 있습니다."));
            } else if(content.contains(badWord)) {
                throw new InvalidRequest(Map.of("content", "비속어가 포함되어 있습니다."));
            }
        }
    }

}
