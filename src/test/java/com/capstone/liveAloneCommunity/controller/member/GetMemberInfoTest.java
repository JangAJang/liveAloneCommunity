package com.capstone.liveAloneCommunity.controller.member;

import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
public class GetMemberInfoTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void initData(){
        memberRepository.deleteAll();
        IntStream.range(1, 11).forEach(i ->
                authService.register(RegisterRequestDto.builder()
                        .username("test"+i)
                        .nickname("test"+i)
                        .email("test"+i+"@test.com")
                        .password("test")
                        .passwordCheck("test").build()));
    }

    @Test
    @DisplayName("회원정보를 성공적으로 불러오면, 200코드와 회원 정보를 반환한다.")
    public void getMemberInfoTest_Success() throws Exception{
        //given
        Long id = 3L;
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member?id="+id)
                .header("Authorization", getAccessToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.username").value("test3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.nickname").value("test3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.email").value("test3@test.com"))
                .andDo(MockMvcResultHandlers.print());
    }

    private String getAccessToken(){
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        return authService.logIn(LogInRequestDto.builder().username("test")
                .password("test").build()).getAccessToken();
    }
}
