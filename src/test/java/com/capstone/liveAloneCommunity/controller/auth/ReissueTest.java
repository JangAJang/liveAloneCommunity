package com.capstone.liveAloneCommunity.controller.auth;

import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.token.TokenResponseDto;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ReissueTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clearDB(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("토큰 재발행에 성공하면 TokenResponseDto를 반환하며 200코드를 반환한다.")
    public void reissueTest_Success() throws Exception{
        //given
        TokenResponseDto tokenResponseDto = getTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(tokenResponseDto))
                .header("Authorization", tokenResponseDto.getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.accessToken").isString())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("헤더에 토큰이 없는 상태로 토큰 재발행을 요청하면 401에러를 반환한다. ")
    public void reissueTest_Fail_UnAuthorized() throws Exception{
        //given
        TokenResponseDto tokenResponseDto = getTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(tokenResponseDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("헤더에 토큰이 조작된 상태(올바르지 않은 상태)로 토큰 재발행을 요청하면 401에러를 반환한다. ")
    public void reissueTest_Fail_Not_Right_Token() throws Exception{
        //given
        TokenResponseDto tokenResponseDto = getTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/auth/reissue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(tokenResponseDto))
                .header("Authorization", tokenResponseDto.getAccessToken()+"a"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String makeJson(Object object){
        try{
            return new ObjectMapper().writeValueAsString(object);
        }catch (Exception e){
            return "";
        }
    }

    private TokenResponseDto getTokenAfterLogIn(){
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        return authService.logIn(LogInRequestDto.builder().username("test").password("test").build());
    }
}
