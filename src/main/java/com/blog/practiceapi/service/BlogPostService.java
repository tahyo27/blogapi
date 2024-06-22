package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.request.PostCreate;

public interface BlogPostService {

    public void write(PostCreate postCreate);

    public Post get(Long id);
}
