package com.blog.practiceapi.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchPagingPost {

    private int page;
    private int size;
}
