package com.blog.practiceapi.repository;

import com.blog.practiceapi.domain.Post;
import com.blog.practiceapi.domain.QPost;
import com.blog.practiceapi.request.CursorPaging;
import com.blog.practiceapi.request.OffsetPaging;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPagingList(OffsetPaging search) { //todo 댓글에서 쓸지 고민
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(search.getSize())
                .offset(search.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }

    @Override
    public List<Post> getCursorPaging(CursorPaging cursorPaging) {


        return jpaQueryFactory.select(Projections.fields(Post.class,
                        QPost.post.id,
                        QPost.post.title,
                        QPost.post.content,
                        QPost.post.regdate
                        ))
                .from(QPost.post)
                .where(ltCursor(cursorPaging.getCursor()))
                .orderBy(QPost.post.id.desc())
                .limit(cursorPaging.getSize())
                .fetch();
    }
    private BooleanExpression ltCursor(Long cursor) {
        if(cursor == null) {
            return null;
        }
        return QPost.post.id.lt(cursor);
    }


}
