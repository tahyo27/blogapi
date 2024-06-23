package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPagingList(int page) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(10)
                .offset((long)(page - 1) * 10)
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
