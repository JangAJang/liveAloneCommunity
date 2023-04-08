package com.capstone.liveAloneCommunity.repository.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

    Page<Comment> findCommentByMember(Member member, Pageable pageable);
    Page<Comment> findCommentByPostId(Long postId, Pageable pageable);
}
