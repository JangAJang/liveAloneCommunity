package com.capstone.liveAloneComunity.controller.member;

import com.capstone.liveAloneComunity.response.Response;
import com.capstone.liveAloneComunity.service.member.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    @ApiOperation(value = "회원 조회", notes = "회원의 DB의 번호, 아이디, 닉네임, 이메일을 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response getMemberInfo(@RequestParam Long id){
        return Response.success(memberService.getMemberInfo(id));
    }
}
