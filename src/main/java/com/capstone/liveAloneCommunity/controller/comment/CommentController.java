package com.capstone.liveAloneCommunity.controller.comment;

import com.capstone.liveAloneCommunity.dto.comment.CommentResponseDto;
import com.capstone.liveAloneCommunity.dto.comment.WriteCommentRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.comment.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private CommentService commentService;
    private MemberRepository memberRepository;

    @GetMapping("")
    @Operation(summary = "댓글 작성", description = "게시물의 댓글을 작성한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response writeComment(@Valid @RequestBody WriteCommentRequestDto writeCommentRequestDto) {
        Member member = memberRepository.findByUsername_Username(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElseThrow(MemberNotAllowedException::new);
        return Response.success(commentService.writeComment(writeCommentRequestDto, member));
    }
}
