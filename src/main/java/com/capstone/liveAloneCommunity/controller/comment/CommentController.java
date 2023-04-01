package com.capstone.liveAloneCommunity.controller.comment;

import com.capstone.liveAloneCommunity.service.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private CommentService commentService;
}
