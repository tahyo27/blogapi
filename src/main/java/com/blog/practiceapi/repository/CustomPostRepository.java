package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;

import java.util.List;

public interface CustomPostRepository {

    List<Post> getPagingList(int page);

}
