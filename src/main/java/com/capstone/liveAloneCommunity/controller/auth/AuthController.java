package com.capstone.liveAloneCommunity.controller.auth;

import com.capstone.liveAloneCommunity.config.jwt.TokenPath;
import com.capstone.liveAloneCommunity.config.oauth.provider.KakaoProfile;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.token.ReissueRequestDto;
import com.capstone.liveAloneCommunity.dto.token.TokenResponseDto;
import com.capstone.liveAloneCommunity.entity.token.OAuthToken;
import com.capstone.liveAloneCommunity.exception.authentication.LogInAgainException;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.auth.KakaoAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenPath tokenPath;
    private final KakaoAuthService kakaoAuthService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "회원가입", description = "회원가입 정보를 기입하면 해당 데이터를 파라미터로 Member를 저장한다.")
    public Response register(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        authService.register(registerRequestDto);
        return Response.success();
    }

    @PostMapping("/logIn")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "로그인", description = "아이디와 비밀번호를 입력하면, 회원이 존재하고 비밀번호가 일치하면 토큰을 반환한다.")
    public Response logIn(@RequestBody @Valid LogInRequestDto logInRequestDto, HttpServletResponse response){
        TokenResponseDto tokenResponseDto = authService.logIn(logInRequestDto);
        tokenPath.putAccessTokenOnHeader(tokenResponseDto.getAccessToken(), response);
        return Response.success();
    }

    @GetMapping("/kakao/callback")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "카카오 로그인", description = "카카오 로그인 진행, 만약 기존에 로그인한 적이 없다면 계정을 만든 후 진행")
    public Response kakaoCallBack(@RequestParam("code") String code, HttpServletResponse response){
        LogInRequestDto logInRequestDto = kakaoAuthService.getLogInRequestByCode(code);
        return logIn(logInRequestDto, response);
    }

    @PatchMapping("/reissue")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "토큰 재발행", description = "Access Token이 만료되었을 때(FrontEnd기준으론 쿠키가 만료되었을 떄) 토큰을 받아와 재발행해준다.")
    public Response reissue(@RequestBody ReissueRequestDto reissueRequestDto){
        return Response.success(authService.reissue(reissueRequestDto));
    }
}
