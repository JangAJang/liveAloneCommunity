package com.capstone.liveAloneCommunity.repository.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Page<Comment> findCommentByMemberId(Long memberId, Pageable pageable);
    Page<Comment> findCommentByPostId(Long postId, Pageable pageable);
}
