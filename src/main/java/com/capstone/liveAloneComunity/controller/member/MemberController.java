package com.capstone.liveAloneComunity.controller.member;

import com.capstone.liveAloneComunity.dto.member.EditMemberInfoDto;
import com.capstone.liveAloneComunity.dto.member.SearchMemberDto;
import com.capstone.liveAloneComunity.entity.Member;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import com.capstone.liveAloneComunity.repository.member.MemberRepositoryCustom;
import com.capstone.liveAloneComunity.response.Response;
import com.capstone.liveAloneComunity.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    @ApiOperation(value = "회원 조회", notes = "회원의 DB의 번호, 아이디, 닉네임, 이메일을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getMemberInfo(@RequestParam Long id){
        return Response.success(memberService.getMemberInfo(id));
    }

    @GetMapping("/search")
    @ApiOperation(value = "회원 검색", notes = "회원의 DB의 번호, 아이디, 닉네임, 이메일을 검색한 값을 포함하는 페이지로 반환한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response searchMember(@RequestBody SearchMemberDto searchMemberDto, @PageableDefault PageRequest pageRequest){
        return Response.success(memberService.searchMember(searchMemberDto, pageRequest));
    }

    @PatchMapping("/edit")
    @ApiOperation(value = "회원 정보 수정", notes = "회원의 이메일, 닉네임을 수정한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response editMemberInfo(@RequestBody EditMemberInfoDto editMemberInfoDto, @RequestParam Long id){
        return Response.success(memberService.editMember(id, editMemberInfoDto, getMember()));
    }

    private Member getMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername_Username(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
    }
}
