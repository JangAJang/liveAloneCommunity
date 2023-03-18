package com.capstone.liveAloneComunity.controller.auth;

import com.capstone.liveAloneComunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.dto.token.ReissueRequestDto;
import com.capstone.liveAloneComunity.response.Response;
import com.capstone.liveAloneComunity.service.auth.AuthService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "회원가입", notes = "회원가입 정보를 기입하면 해당 데이터를 파라미터로 Member를 저장한다.")
    public Response register(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        authService.register(registerRequestDto);
        return Response.success();
    }

    @PostMapping("/logIn")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "로그인", notes = "아이디와 비밀번호를 입력하면, 회원이 존재하고 비밀번호가 일치하면 토큰을 반환한다.")
    public Response logIn(@RequestBody LogInRequestDto logInRequestDto){
        return Response.success(authService.logIn(logInRequestDto));
    }

    @PatchMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "토큰 재발행", notes = "Access Token이 만료되었을 때(FrontEnd기준으론 쿠키가 만료되었을 떄) 토큰을 받아와 재발행해준다.")
    public Response reissue(@RequestBody ReissueRequestDto reissueRequestDto){
        return Response.success(authService.reissue(reissueRequestDto));
    }
}
