package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.domain.QPost;
import com.blog.practiceapi.request.SearchPagingPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
                .offset(search.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }

    @Override
    public List<Post> getCursorPaging(Long cursor) {


        return jpaQueryFactory.select(Projections.fields(Post.class,
                        QPost.post.title,
                        QPost.post.content
                        ))
                .from(QPost.post)
                .where(ltCursor(cursor))
                .orderBy(QPost.post.id.desc())
                .limit(100)
                .fetch();
    }
    private BooleanExpression ltCursor(Long cursor) {
        if(cursor == null) {
            return null;
        }
        return QPost.post.id.lt(cursor);
    }


}
