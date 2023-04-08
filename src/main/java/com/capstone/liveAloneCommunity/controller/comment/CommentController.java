package com.capstone.liveAloneCommunity.controller.comment;

import com.capstone.liveAloneCommunity.dto.comment.CommentPageInfoRequestDto;
import com.capstone.liveAloneCommunity.dto.comment.ReadCommentByPostRequestDto;
import com.capstone.liveAloneCommunity.dto.comment.WriteCommentRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public Response readCommentByMember() {
        Member member = getMember();
        return Response.success(commentService.readCommentByMember(member));
    }

    @GetMapping("/post")
    @Operation(summary = "게시물로 댓글 조회", description = "게시물 id로 회원이 작성한 댓글을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readCommentByPost(@Valid @RequestBody ReadCommentByPostRequestDto readCommentByPostRequestDto) {
        return Response.success(commentService.readCommentByPost(readCommentByPostRequestDto));
    }

    private Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername_Username(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
    }
}
