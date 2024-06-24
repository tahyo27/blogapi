package com.blog.practiceapi.service;

import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.blog.practiceapi.response.BlogPostResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogPostService {

    public void write(CreatePost createPost);

    public BlogPostResponse get(Long id);

    public List<BlogPostResponse> getList(SearchPagingPost search);
}
