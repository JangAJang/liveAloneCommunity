package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.dto.comment.*;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto writeComment(WriteCommentRequestDto writeCommentRequestDto, Member member) {
        Post post = getPost(writeCommentRequestDto);
        Comment comment = new Comment(writeCommentRequestDto.getContent(), post, member);
        commentRepository.save(comment);
        return CommentResponseDto.toDto(comment);
    }

    public MultiReadCommentResponseDto readCommentByMember(Member member) {
        List<Comment> findCommentByMember = commentRepository.findCommentByMember(member);
        return collectComment(findCommentByMember);
    }

    public MultiReadCommentResponseDto readCommentByPost(ReadCommentByPostRequestDto readCommentByPostRequestDto) {
        List<Comment> findCommentByPost = commentRepository.findCommentByPostId(readCommentByPostRequestDto.getPostId());
        return collectComment(findCommentByPost);
    }

    private Post getPost(WriteCommentRequestDto writeCommentRequestDto) {
        return postRepository.findById(writeCommentRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);
    }

    private MultiReadCommentResponseDto collectComment(List<Comment> findComment) {
        return new MultiReadCommentResponseDto(findComment.stream()
                .map(ReadCommentResponseDto::toDto)
                .collect(Collectors.toList()));
    }
}
