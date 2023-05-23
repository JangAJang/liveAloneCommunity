package com.capstone.liveAloneCommunity.controller.comment;

import com.capstone.liveAloneCommunity.dto.comment.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;
    private final MemberRepository memberRepository;

    @PostMapping("")
    @Operation(summary = "댓글 작성", description = "게시물의 댓글을 작성한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response writeComment(@Valid @RequestBody WriteCommentRequestDto writeCommentRequestDto) {
        Member member = getMember();
        return Response.success(commentService.writeComment(writeCommentRequestDto, member));
    }

    @GetMapping("/member")
    @Operation(summary = "멤버로 댓글 조회", description = "멤버 id로 회원이 작성한 댓글을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readCommentByMemberId(@PageableDefault Pageable pageable) {
        Member member = getMember();
        CommentPageInfoRequestDto commentPageInfoRequestDto =
                new CommentPageInfoRequestDto(pageable.getPageNumber(), pageable.getPageSize());
        return Response.success(commentService.readCommentByMemberId(member, commentPageInfoRequestDto));
    }

    @GetMapping("/post")
    @Operation(summary = "게시물로 댓글 조회", description = "게시물 id로 회원이 작성한 댓글을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readCommentByPostId(@RequestParam Long postId, @PageableDefault Pageable pageable) {
        ReadCommentByPostRequestDto readCommentByPostRequestDto =
                new ReadCommentByPostRequestDto(postId, new CommentPageInfoRequestDto(pageable.getPageNumber(), pageable.getPageSize()));
        return Response.success(commentService.readCommentByPostId(readCommentByPostRequestDto));
    }

    @PatchMapping("/edit")
    @Operation(summary = "댓글 수정", description = "댓글 작성자는 자신의 댓글을 삭제할 수 있다.")
    @ResponseStatus(HttpStatus.OK)
    public Response editComment(@Valid @RequestBody EditCommentRequestDto editCommentRequestDto) {
        Member member = getMember();
        return Response.success(commentService.editComment(member, editCommentRequestDto));
    }

    @DeleteMapping("")
    @Operation(summary = "댓글 삭제", description = "댓글 작성자는 자신의 댓글을 삭제할 수 있다.")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteComment(@RequestParam Long id) {
        Member member = getMember();
        commentService.deleteComment(member, new DeleteCommentRequestDto(id));
        return Response.success("삭제 완료");
    }

    private Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername_Username(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
    }
}
