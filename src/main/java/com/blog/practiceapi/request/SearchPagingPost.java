package com.blog.practiceapi.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.*;

@Getter
public class SearchPagingPost {

    private static final int MAX_PAGE_SIZE = 1000;

    private final int page;
    private final int size;

    @Builder
    public SearchPagingPost(Integer page, Integer size) { //todo 페이징 조건처리 페이징 오프셋 없앨지 생각
        this.page = (page == null || page < 1)? 1 : page;
        this.size = (size == null || size < 10 || size > MAX_PAGE_SIZE)? 10 : size;
    }

    public long getOffset() {
        return (long) (page - 1) * size;
    }
}
