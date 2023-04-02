package com.capstone.liveAloneCommunity.repository.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> searchCommentByPostId(Long postId);
}
