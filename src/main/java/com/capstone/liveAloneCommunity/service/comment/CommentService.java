package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.dto.comment.*;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Direction.*;

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

    @Transactional(readOnly = true)
    public MultiReadCommentResponseDto readCommentByMember(Member member, CommentPageInfoRequestDto commentPageInfoRequestDto) {
        Page<Comment> commentByMember = commentRepository.findCommentByMemberId(member.getId(), getPageRequestComment(commentPageInfoRequestDto));
        return collectComment(commentByMember.getContent());
    }

    @Transactional(readOnly = true)
    public MultiReadCommentResponseDto readCommentByPost(ReadCommentByPostRequestDto readCommentByPostRequestDto) {
        Page<Comment> commentByPostId = commentRepository.findCommentByPostId(readCommentByPostRequestDto.getPostId(),
                getPageRequestComment(readCommentByPostRequestDto.getCommentPageInfoRequestDto()));
        return collectComment(commentByPostId.getContent());
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

    private Pageable getPageRequestComment(CommentPageInfoRequestDto commentPageInfoRequestDto) {
        return PageRequest.of(commentPageInfoRequestDto.getPage(), commentPageInfoRequestDto.getSize(),
                Sort.by(DESC, "createdDate"));
    }
}
