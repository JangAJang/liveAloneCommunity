package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private CommentRepository commentRepository;
}
