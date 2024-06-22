package com.blog.practiceapi.service;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.request.PostCreate;
import com.blog.practiceapi.response.PostResponse;

import java.util.List;

public interface BlogPostService {

    public void write(PostCreate postCreate);

    public PostResponse get(Long id);

    public List<PostResponse> getList();
}
