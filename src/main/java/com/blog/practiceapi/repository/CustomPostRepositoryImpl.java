package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.domain.QPost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPagingList(SearchPagingPost search) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(search.getSize())
                .offset((long)(search.getPage() - 1) * search.getSize())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
