package com.capstone.liveAloneCommunity.controller;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private String makeJson(Object object){
        try{
            return new ObjectMapper().writeValueAsString(object);
        }catch (Exception e){
            return "";
        }
    }
}
