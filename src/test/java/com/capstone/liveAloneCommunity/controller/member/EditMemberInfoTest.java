package com.capstone.liveAloneCommunity.controller.member;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.member.EditNicknameDto;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class EditMemberInfoTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void initTestData(){
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
    }

    @Test
    @DisplayName("토큰을 가지고 있는 상태에서 닉네임을 수정하면 200코드와 정상적으로 수정됨을 반환한다.")
    public void editNicknameTest_Success() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn();
        EditNicknameDto editNicknameDto = new EditNicknameDto("newNick");
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/edit")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(editNicknameDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String makeJson(Object object)throws Exception{
        return new ObjectMapper().writeValueAsString(object);
    }

    private String getAccessTokenAfterLogIn(){
        return authService.logIn(LogInRequestDto.builder()
                        .username("test")
                        .password("test")
                        .build())
                .getAccessToken();
    }

    @AfterEach
    void clearDB(){
        databaseCleanup.execute();
    }
}
