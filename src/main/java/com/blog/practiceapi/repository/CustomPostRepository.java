package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.request.CursorPaging;
import com.blog.practiceapi.request.OffsetPaging;

import java.util.List;

public interface CustomPostRepository {

    List<Post> getPagingList(OffsetPaging search);

    List<Post> getCursorPaging(CursorPaging cursorPaging);

}
