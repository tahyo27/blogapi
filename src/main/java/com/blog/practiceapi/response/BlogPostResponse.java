package com.blog.practiceapi.response;

import com.blog.practiceapi.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BlogPostResponse {

    private final Long id;
    private final String title;
    private final String content;

    public BlogPostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public BlogPostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
