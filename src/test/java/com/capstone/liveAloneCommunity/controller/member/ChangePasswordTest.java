package com.capstone.liveAloneCommunity.controller.member;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.member.ChangePasswordRequestDto;
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
class ChangePasswordTest {

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
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
    }

    @AfterEach
    void clearDB(){
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 현재 비밀번호, 새 비밀번호, 새 비밀번호 재입력을 올바르게 입력하면 200코드와 비밀번호 변경이 성공함을 반환한다.")
    public void changePasswordTest_Success() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("test")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 없는 상태에서 현재 비밀번호, 새 비밀번호, 새 비밀번호 재입력을 올바르게 입력하면 401코드와 다시 로그인해야함을 알려준다.")
    public void changePasswordTest_Fail_Unauthorized() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("test")
                .newPasswordCheck("test").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 현재 비밀번호가 null이면 400에러와 기존 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Null_Current_Password() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .newPassword("test")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("기존 비밀번호를 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 현재 비밀번호가 빈 문자열이면 400에러와 기존 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Empty_Current_Password() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("")
                .newPassword("test")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("기존 비밀번호를 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 현재 비밀번호가 공백 문자열이면 400에러와 기존 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Blank_Current_Password() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("  ")
                .newPassword("test")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("기존 비밀번호를 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 입력한 현재 비밀번호가 실제 회원의 비밀번호와 다르면 400에러와 비밀번호를 다시 확인해야함을 알려준다")
    public void changePasswordTest_Fail_Different_Current_Password() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test1")
                .newPassword("test")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("비밀번호를 다시 확인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 새 비밀번호가 null이면 400코드와 새 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Null_New_Password() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("새 비밀번호를 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 새 비밀번호가 빈 문자열이면 400코드와 새 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Empty_New_Password() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("새 비밀번호를 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 새 비밀번호가 공백 문자열이면 400코드와 새 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Blank_New_Password() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("  ")
                .newPasswordCheck("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("새 비밀번호를 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 새 비밀번호 다시 입력이 null이면 400코드와 새 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Null_New_Password_Check() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("test").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("새 비밀번호를 다시 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 새 비밀번호 다시 입력이 빈 문자열이면 400코드와 새 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Empty_New_Password_Check() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("test")
                .newPasswordCheck("").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("새 비밀번호를 다시 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 새 비밀번호 다시 입력이 공백 문자열이면 400코드와 새 비밀번호를 입력해야함을 알려준다.")
    public void changePasswordTest_Fail_Blank_New_Password_Check() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("test")
                .newPasswordCheck("   ").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("새 비밀번호를 다시 입력해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태에서 새 비밀번호와 새 비밀번호 재입력이 일치하지 않으면 400코드와 비밀번호가 일치하지 않음을 알려준다.")
    public void changePasswordTest_Fail_PasswordNotEqual() throws Exception{
        //given
        ChangePasswordRequestDto changePasswordRequestDto = ChangePasswordRequestDto.builder()
                .currentPassword("test")
                .newPassword("test")
                .newPasswordCheck("test1").build();
        String accessToken = getAccessTokenAfterLogIn();
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/member/changePassword")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(changePasswordRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("비밀번호가 일치하지 않습니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String getAccessTokenAfterLogIn(){
        return authService.logIn(LogInRequestDto
                .builder().username("test").password("test").build())
                .getAccessToken();
    }

    private String makeJson(Object object)throws Exception{
        return new ObjectMapper().writeValueAsString(object);
    }
}
