package com.capstone.liveAloneCommunity.controller.post;

import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.dto.post.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.post.SearchPostType;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.post.PostService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
public class PostController {

    private final PostService postService;
    private final MemberRepository memberRepository;

    @GetMapping("")
    @Operation(summary = "게시물 단건 조회", description = "게시물 단건을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getPost(@RequestParam("id") Long id){
        return Response.success(postService.getPost(id));
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리별 게시물 조회", description = "게시물을 카테고리별로 페이징처리해 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getPostOfCategory(@RequestBody PostByCategoryRequestDto postByCategoryRequestDto){
        return Response.success(postService.getPostByCategory(postByCategoryRequestDto));
    }

    @GetMapping("/search")
    @Operation(summary = "게시물 검색", description = "게시물을 검색해 페이징처리해 반환한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response searchPost(@RequestBody @Valid SearchPostRequestDto searchPostRequestDto){
        return Response.success(postService.searchPost(searchPostRequestDto));
    }

    @GetMapping("/of")
    @Operation(summary = "회원의 게시물 조회", description = "회원의 게시물을 이름의 역순으로 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getMembersPost(@PageableDefault Pageable pageable){
        MembersPostRequestDto membersPostRequestDto = MembersPostRequestDto.builder()
                .id(getMember().getId())
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize()).build();
        return Response.success(postService.getMembersPost(membersPostRequestDto));
    }

    @PostMapping("/write")
    @Operation(summary = "게시글 작성", description = "게시글을 새로 작성한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response writePost(@RequestBody @Valid WritePostRequestDto writePostRequestDto){
        Member member = getMember();
        return Response.success(postService.writePost(member, writePostRequestDto));
    }

    private Member getMember() {
        return memberRepository.findByUsername_Username(SecurityContextHolder.getContext()
                .getAuthentication().getName()).orElseThrow(MemberNotFoundException::new);
    }

    @PatchMapping("/edit")
    @Operation(summary = "게시글 수정", description = "존재하는 게시물을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response editPost(@RequestBody @Valid EditPostRequestDto editPostRequestDto){
        Member member = getMember();
        return Response.success(postService.editPost(editPostRequestDto, member));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response deletePost(@RequestParam("id") Long id){
        Member member = getMember();
        postService.deletePost(id, member);
        return Response.success();
    }
}
