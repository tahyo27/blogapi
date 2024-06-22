package com.blog.practiceapi.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Map;

@Getter
public class ValidationError {

    @JsonProperty("title")
    private final String title;
    @JsonProperty("content")
    private final String content;

    public ValidationError(Map<String, String> map) {
        this.title = map.get("title");
        this.content = map.get("content");
    }
}
