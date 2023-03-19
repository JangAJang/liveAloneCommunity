package com.capstone.liveAloneComunity.service.authentication;

import com.capstone.liveAloneComunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.dto.token.ReissueRequestDto;
import com.capstone.liveAloneComunity.dto.token.TokenResponseDto;
import com.capstone.liveAloneComunity.exception.member.*;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import com.capstone.liveAloneComunity.repository.refreshToken.RefreshTokenRepository;
import com.capstone.liveAloneComunity.service.auth.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void clearDB(){
        memberRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입을 시도할 때, 해당 데이터가 존재하지 않는다면 성공적으로 데이터를 저장해 리포지토리 크기가 1 증가한다.. ")
    public void registerTest() throws Exception{
        //given
        long size = memberRepository.count();
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("username")
                .nickname("nickname")
                .email("email@email.com")
                .password("password")
                .passwordCheck("password").build();
        //when
        authService.register(registerRequestDto);
        //then
        Assertions.assertThat(memberRepository.count()).isEqualTo(size + 1);
    }

    @Test
    @DisplayName("이메일 형식이 어긋나면, 회원가입에 실패한다.")
    public void registerFail_EmailFormat() throws Exception{
        //given
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("username")
                .nickname("nickname")
                .email("email.com")
                .password("password")
                .passwordCheck("password").build();
        //when

        //then
        Assertions.assertThatThrownBy(() -> authService.register(registerRequestDto))
                .isInstanceOf(EmailNotFormatException.class);
    }

    @Test
    @DisplayName("입력한 두 비밀번호가 일치하지 않을 때, 회원가입에 실패한다.")
    public void registerFail_PasswordNotEqual() throws Exception{
        //given
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("username")
                .nickname("nickname")
                .email("email@email.com")
                .password("password")
                .passwordCheck("password1").build();
        //when

        //then
        Assertions.assertThatThrownBy(() -> authService.register(registerRequestDto))
                .isInstanceOf(PasswordNotMatchingException.class);
    }

    @Test
    @DisplayName("이미 존재하는 아이디로 회원가입 할 때, 회원가입에 실패한다.")
    public void registerFail_UsernameDuplicated() throws Exception{
        //given
        createDummyMember();
        //when
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("username")
                .nickname("nickname1")
                .email("email1@email.com")
                .password("password1")
                .passwordCheck("password1").build();
        //then
        Assertions.assertThatThrownBy(() -> authService.register(registerRequestDto))
                .isInstanceOf(UsernameAlreadyInUseException.class);
    }

    @Test
    @DisplayName("이미 존재하는 닉네임으로 회원가입 할 때, 회원가입에 실패한다.")
    public void registerFail_NicknameDuplicated() throws Exception{
        //given
        createDummyMember();
        //when
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("username1")
                .nickname("nickname")
                .email("email1@email.com")
                .password("password1")
                .passwordCheck("password1").build();
        //then
        Assertions.assertThatThrownBy(() -> authService.register(registerRequestDto))
                .isInstanceOf(NicknameAlreadyInUseException.class);
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 할 때, 회원가입에 실패한다.")
    public void registerFail_EmailDuplicated() throws Exception{
        //given
        createDummyMember();
        //when
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("username1")
                .nickname("nickname1")
                .email("email@email.com")
                .password("password1")
                .passwordCheck("password1").build();
        //then
        Assertions.assertThatThrownBy(() -> authService.register(registerRequestDto))
                .isInstanceOf(EmailAlreadyInUseException.class);
    }

    @Test
    @DisplayName("로그인을 시도할 떄, 아이디와 비밀번호를 정상적으로 입력하면 로그인 토큰을 반환한다.")
    public void logInTest() throws Exception{
        //given
        createDummyMember();
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("username")
                .password("password").build();
        //when
        TokenResponseDto tokenResponseDto = authService.logIn(logInRequestDto);
        //then
        Assertions.assertThat(StringUtils.hasText(tokenResponseDto.getAccessToken())).isTrue();
        Assertions.assertThat(StringUtils.hasText(tokenResponseDto.getRefreshToken())).isTrue();
    }

    @Test
    @DisplayName("로그인을 시도할 떄, 아이디와 비밀번호를 정상적으로 입력하면 로그인 토큰을 반환한다.")
    public void logInFail_NoMember() throws Exception{
        //given
        createDummyMember();
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("username1")
                .password("password").build();
        //when

        //then
        Assertions.assertThatThrownBy(() -> authService.logIn(logInRequestDto))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("로그인을 시도할 떄, 아이디와 비밀번호를 정상적으로 입력하면 로그인 토큰을 반환한다.")
    public void logInFail_PasswordUnMatch() throws Exception{
        //given
        createDummyMember();
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("username")
                .password("password1").build();
        //when

        //then
        Assertions.assertThatThrownBy(() -> authService.logIn(logInRequestDto))
                .isInstanceOf(PasswordNotMatchingException.class);
    }

    @Test
    @DisplayName("토큰 재발행을 요청할 때, 정상적인 토큰이 존재할 경우, 토큰을 재발행해준다. ")
    public void reissueTest() throws Exception{
        //given
        createDummyMember();
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("username")
                .password("password").build();
        //when
        TokenResponseDto tokenResponseDto = authService.logIn(logInRequestDto);
        TokenResponseDto reissueResponse = authService.reissue(ReissueRequestDto.builder()
                .accessToken(tokenResponseDto.getAccessToken())
                .refreshToken(tokenResponseDto.getRefreshToken()).build());
        //then
        Assertions.assertThat(StringUtils.hasText(reissueResponse.getAccessToken())).isTrue();
        Assertions.assertThat(StringUtils.hasText(reissueResponse.getRefreshToken())).isTrue();
    }

    @Test
    @DisplayName("토큰 재발행을 요청할 때, 정상적인 토큰이 존재할 경우, 토큰을 재발행해준다. ")
    public void reissueFail_() throws Exception{
        //given
        createDummyMember();
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("username")
                .password("password").build();
        //when
        TokenResponseDto tokenResponseDto = authService.logIn(logInRequestDto);
        TokenResponseDto reissueResponse = authService.reissue(ReissueRequestDto.builder()
                .accessToken(tokenResponseDto.getAccessToken())
                .refreshToken(tokenResponseDto.getRefreshToken()).build());
        //then
        Assertions.assertThat(StringUtils.hasText(reissueResponse.getAccessToken())).isTrue();
        Assertions.assertThat(StringUtils.hasText(reissueResponse.getRefreshToken())).isTrue();
    }

    private void createDummyMember(){
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("username")
                .nickname("nickname")
                .email("email@email.com")
                .password("password")
                .passwordCheck("password").build();
        authService.register(registerRequestDto);
    }
}
