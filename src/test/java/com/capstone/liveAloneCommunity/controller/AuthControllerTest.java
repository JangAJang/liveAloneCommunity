package com.capstone.liveAloneCommunity.controller;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
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

@AutoConfigureMockMvc
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void clearDB(){
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공시, 200코드가 반환된다.")
    public void registerTest_Success() throws Exception{
        //given
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test")
                .build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(registerRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원가입 실행시 이메일 형식이 올바르지 않으면 예외처리한다. ")
    public void registerTest_FAIL_EMAIL_FORMAT() throws Exception{
        //given
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("testtest.com")
                .password("test")
                .passwordCheck("test")
                .build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(registerRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("올바르지 않은 이메일 형식입니다."))
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
