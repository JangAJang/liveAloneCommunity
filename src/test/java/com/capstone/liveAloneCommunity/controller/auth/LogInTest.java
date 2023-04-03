package com.capstone.liveAloneCommunity.controller.auth;

import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
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
public class LogInTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void initDB(){
        memberRepository.deleteAll();
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("former")
                .nickname("former")
                .email("former@former.com")
                .password("former")
                .passwordCheck("former")
                .build();
        authService.register(registerRequestDto);
    }

    @Test
    @DisplayName("올바른 아이디와 비밀번호를 입력하면 로그인을 성공하며 토큰을 반환한다.")
    public void logInTest_Success() throws Exception{
        //given
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("former")
                .password("former").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/logIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(logInRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.accessToken").exists())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("아이디가 null값이면 400에러와 \"아이디를 입력하세요\" 문구를 출력한다.")
    public void logInTest_Fail_Null_Username() throws Exception{
        //given
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .password("former").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/logIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(logInRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("아이디를 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("아이디가 빈 문자열이면 400에러와 \"아이디를 입력하세요\" 문구를 출력한다.")
    public void logInTest_Fail_Empty_Username() throws Exception{
        //given
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("")
                .password("former").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/logIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(logInRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("아이디를 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("아이디가 공백문자열이면 400에러와 \"아이디를 입력하세요\" 문구를 출력한다.")
    public void logInTest_Fail_Blank_Username() throws Exception{
        //given
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("   ")
                .password("former").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/logIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(logInRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("아이디를 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String makeJson(Object object){
        try{
            return new ObjectMapper().writeValueAsString(object);
        }catch (Exception e){
            return "";
        }
    }
}
