package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.dto.comment.CommentResponseDto;
import com.capstone.liveAloneCommunity.dto.comment.WriteCommentRequestDto;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto writeComment(WriteCommentRequestDto writeCommentRequestDto, Member member) {
        Post post = postRepository.findById(writeCommentRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment(writeCommentRequestDto.getContent(), post, member);
        commentRepository.save(comment);
        return CommentResponseDto.toDto(comment);
    }
}
