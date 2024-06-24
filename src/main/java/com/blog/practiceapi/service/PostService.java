package com.blog.practiceapi.service;

import com.blog.practiceapi.request.CreatePost;
import com.blog.practiceapi.request.EditPost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.blog.practiceapi.response.PostResponse;

import java.util.List;

public interface PostService {

    void write(CreatePost createPost);

    PostResponse get(Long id);

    List<PostResponse> getList(SearchPagingPost search);

    void editPost(Long id, EditPost editPost);
}
