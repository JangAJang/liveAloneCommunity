package com.capstone.liveAloneCommunity.repository.comment;

import com.capstone.liveAloneCommunity.controller.comment.CommentController;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentController, Long> {

}
