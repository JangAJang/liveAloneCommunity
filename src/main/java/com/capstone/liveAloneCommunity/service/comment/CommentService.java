package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.dto.comment.*;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
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

    public MultiReadCommentResponseDto readCommentByMember(CommentPageInfoRequestDto commentPageInfoRequestDto, Member member) {
        List<Comment> commentByMember = commentRepository
                .findCommentByMember(member, getPageRequestComment(commentPageInfoRequestDto)).getContent();
        return collectComment(commentByMember);
    }

    public MultiReadCommentResponseDto readCommentByPost(ReadCommentByPostRequestDto readCommentByPostRequestDto) {
        List<Comment> commentByPostId = commentRepository.findCommentByPostId(readCommentByPostRequestDto.getPostId(),
                getPageRequestComment(readCommentByPostRequestDto.getCommentPageInfoRequestDto())).getContent();
        return collectComment(commentByPostId);
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
