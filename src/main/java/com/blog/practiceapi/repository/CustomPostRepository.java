package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.request.SearchPagingPost;

import java.util.List;

public interface CustomPostRepository {

    List<Post> getPagingList(SearchPagingPost search);

    List<Post> getCursorPaging(Long cursor);

}
