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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.username").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.nickname").value("newNick"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.email").value("test@test.com"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰을 가지고 있는 상태에서 닉네임을 null로 수정하면 400코드와 닉네임을 입력해야함을 알려준다.")
    public void editNicknameTest_Fail_Null_Nickname() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn();
        EditNicknameDto editNicknameDto = EditNicknameDto.builder().build();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/edit")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(editNicknameDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("닉네임을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰을 가지고 있는 상태에서 닉네임을 빈 문자열로 수정하면 400코드와 닉네임을 입력해야함을 알려준다.")
    public void editNicknameTest_Fail_Empty_Nickname() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn();
        EditNicknameDto editNicknameDto = EditNicknameDto.builder()
                .nickname("").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/edit")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(editNicknameDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("닉네임을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰을 가지고 있는 상태에서 닉네임을 공백 문자열로 수정하면 400코드와 닉네임을 입력해야함을 알려준다.")
    public void editNicknameTest_Fail_Blank_Nickname() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn();
        EditNicknameDto editNicknameDto = EditNicknameDto.builder()
                .nickname("  ").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/edit")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(editNicknameDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("닉네임을 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 없는 상태에서 닉네임을 수정하면 401코드와 다시 로그인해야 함을 알려준다.")
    public void editNicknameTest_Fail_Unauthorized() throws Exception{
        //given
        EditNicknameDto editNicknameDto = new EditNicknameDto("newNick");
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(editNicknameDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
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
