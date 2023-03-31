package com.capstone.liveAloneCommunity.controller.member;

import com.capstone.liveAloneCommunity.dto.member.ChangePasswordRequestDto;
import com.capstone.liveAloneCommunity.dto.member.EditMemberInfoDto;
import com.capstone.liveAloneCommunity.dto.member.SearchMemberDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("")
    @Operation(summary = "회원 조회", description = "회원의 DB의 번호, 아이디, 닉네임, 이메일을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getMemberInfo(@RequestParam Long id){
        return Response.success(memberService.getMemberInfo(id));
    }

    @GetMapping("/search")
    @Operation(summary = "회원 검색", description = "회원의 DB의 번호, 아이디, 닉네임, 이메일을 검색한 값을 포함하는 페이지로 반환한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response searchMember(@RequestBody @Valid SearchMemberDto searchMemberDto){
        return Response.success(memberService.searchMember(searchMemberDto));
    }

    @PatchMapping("/edit")
    @Operation(summary = "회원 정보 수정", description = "회원의 이메일, 닉네임을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response editMemberInfo(@RequestBody EditMemberInfoDto editMemberInfoDto, @RequestParam Long id){
        return Response.success(memberService.editMember(id, editMemberInfoDto, getMember()));
    }

    @PatchMapping("/changePassword")
    @Operation(summary = "비밀번호 변경", description = "현재비밀번호, 새 비밀번호, 새 비밀번호 다시 입력을 입력하면 비밀번호를 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response changePassword(@RequestBody @Valid ChangePasswordRequestDto changePasswordRequestDto){
        memberService.changePassword(getMember(), changePasswordRequestDto);
        return Response.success();
    }

    @DeleteMapping("/delete")
    @Operation(summary = "회원 삭제", description = "회원 삭제(회원 탈퇴)를 수행한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response deleteMember(@RequestParam Long id){
        memberService.deleteMember(id, getMember());
        return Response.success();
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername_Username(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
    }
}
