package com.capstone.liveAloneCommunity.repository.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import java.util.List;
import static com.capstone.liveAloneCommunity.entity.comment.QComment.*;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> searchCommentByPostId(Long postId) {
        return jpaQueryFactory
                .select(comment)
                .from(comment)
                .where(comment.post.id.eq(postId))
                .fetch();
    }
}
