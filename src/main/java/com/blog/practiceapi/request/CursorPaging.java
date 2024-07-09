package com.blog.practiceapi.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CursorPaging {
    private static final int MAX_PAGE_SIZE = 100;

    private final Long cursor;
    private final int size;

    @Builder
    public CursorPaging(Long cursor, Integer size) {
        this.cursor = cursor;
        this.size = (size == null || size < 10 || size > MAX_PAGE_SIZE) ? 10 : size;
    }
}
