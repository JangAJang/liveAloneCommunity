package com.capstone.liveAloneCommunity.controller.member;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import org.junit.jupiter.api.AfterEach;
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

@SpringBootTest
@AutoConfigureMockMvc
class DeleteMemberTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void initData(){
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .password("test")
                .passwordCheck("test")
                .email("test@test.com").build());
    }

    @AfterEach
    void clearDB(){
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("회원을 삭제할 때 토큰이 있으면 200코드와 함꼐 성공적으로 회원이 삭제됨을 반환한다.")
    public void deleteMemberTest_Success() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.delete("/api/member/delete")
                .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원을 삭제할 때 토큰이 없으면 401코드와 다시 로그인해야함을 알려준다.")
    public void deleteMemberTest_Fail_Unauthorized() throws Exception{
        //given
        //expected
        mvc.perform(MockMvcRequestBuilders.delete("/api/member/delete"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String getAccessTokenAfterLogIn(){
        return authService.logIn(LogInRequestDto.builder().username("test").password("test").build()).getAccessToken();
    }
}
