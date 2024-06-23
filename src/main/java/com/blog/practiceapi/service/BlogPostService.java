package com.blog.practiceapi.service;

import com.blog.practiceapi.request.PostCreate;
import com.blog.practiceapi.response.BlogPostResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogPostService {

    public void write(PostCreate postCreate);

    public BlogPostResponse get(Long id);

    public List<BlogPostResponse> getList(Pageable pageable);
}
