package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.dto.comment.*;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.comment.NotMyCommentException;
import com.capstone.liveAloneCommunity.exception.comment.CommentNotFoundException;
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

import static org.springframework.data.domain.Sort.Direction.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto writeComment(final WriteCommentRequestDto writeCommentRequestDto, final Member member) {
        Post post = getPost(writeCommentRequestDto);
        Comment comment = new Comment(writeCommentRequestDto.getContent(), post, member);
        commentRepository.save(comment);
        return CommentResponseDto.from(comment);
    }

    @Transactional(readOnly = true)
    public MultiReadCommentResponseDto readCommentByMemberId(final Member member, final CommentPageInfoRequestDto commentPageInfoRequestDto) {
        Page<Comment> commentByMember = commentRepository.findCommentByMemberId(member.getId(), getPageRequestComment(commentPageInfoRequestDto));
        return collectComment(commentByMember);
    }

    @Transactional(readOnly = true)
    public MultiReadCommentResponseDto readCommentByPostId(final ReadCommentByPostRequestDto readCommentByPostRequestDto) {
        Page<Comment> commentByPostId = commentRepository.findCommentByPostId(readCommentByPostRequestDto.getPostId(),
                getPageRequestComment(readCommentByPostRequestDto.getCommentPageInfoRequestDto()));
        return collectComment(commentByPostId);
    }

    public CommentResponseDto editComment(final Member member, final EditCommentRequestDto editCommentRequestDto) {
        Comment comment = getCommentById(editCommentRequestDto.getCommentId());
        validateCommentAuthority(member, comment);
        comment.editContent(new Content(editCommentRequestDto.getModifyContent()));
        return CommentResponseDto.from(comment);
    }

    public void deleteComment(final Member member, final DeleteCommentRequestDto deleteCommentRequestDto) {
        Comment comment = getCommentById(deleteCommentRequestDto.getCommentId());
        validateCommentAuthority(member, comment);
        commentRepository.delete(comment);
    }

    private Post getPost(final WriteCommentRequestDto writeCommentRequestDto) {
        return postRepository.findById(writeCommentRequestDto.getPostId())
                .orElseThrow(PostNotFoundException::new);
    }

    private MultiReadCommentResponseDto collectComment(final Page<Comment> findComment) {
        return MultiReadCommentResponseDto.from(findComment.map(ReadCommentResponseDto::toDto));
    }

    private Pageable getPageRequestComment(final CommentPageInfoRequestDto commentPageInfoRequestDto) {
        return PageRequest.of(commentPageInfoRequestDto.getPage(), commentPageInfoRequestDto.getSize(),
                Sort.by(DESC, "createdDate"));
    }

    private Comment getCommentById(final Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    private void validateCommentAuthority(final Member member, final Comment comment) {
        if (!comment.isWriter(member)) {
            throw new NotMyCommentException();
        }
    }
}
