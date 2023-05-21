package com.capstone.liveAloneCommunity.controller.message;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.message.WriteMessageRequestDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class WriteMessageTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void initData() {
        registerMember("test");
    }

    @AfterEach
    void clearDB() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("토큰이 있고 요청이 올바르게 왔을 경우 200코드와 작성된 쪽지가 반환된다.")
    void writeMessage_Success() throws Exception{
        // given
        registerMember("receiver");
        WriteMessageRequestDto writeMessageRequestDto = new WriteMessageRequestDto("message", "receiver");

        // when // then
        mvc.perform(post("/api/message")
                        .header("Authorization", getAccessTokenAfterLogIn("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeMessageRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.content").value("message"))
                .andExpect(jsonPath("$.result.data.receiver").value("receiver"))
                .andExpect(jsonPath("$.result.data.sender").value("test"))
                .andExpect(jsonPath("$.result.data.createDate").isString())
                .andDo(print());
    }

    @Test
    @DisplayName("토큰이 없는 경우 401코드와 다시 로그인하라는 메세지가 반환된다.")
    void writeMessage_Fail_Unauthorized() throws Exception{
        // given
        registerMember("receiver");
        WriteMessageRequestDto writeMessageRequestDto = new WriteMessageRequestDto("message", "receiver");

        // when, // then
        mvc.perform(post("/api/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeMessageRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있고 수신자가 null일 경우 400에러와 수신자를 입력하라는 메서지가 반환된다")
    void writeMessage_Fail_Null_Receiver() throws Exception{
        // given
        registerMember("receiver");
        WriteMessageRequestDto writeMessageRequestDto = new WriteMessageRequestDto("message", null);

        // when // then
        mvc.perform(post("/api/message")
                        .header("Authorization", getAccessTokenAfterLogIn("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeMessageRequestDto)))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.failMessage").value("수신자를 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("토큰이 있고 수신자가 빈 문자열인 경우 400에러와 수신자를 입력하라는 메서지가 반환된다")
    void writeMessage_Fail_Empty_Receiver() throws Exception{
        // given
        registerMember("receiver");
        WriteMessageRequestDto writeMessageRequestDto = new WriteMessageRequestDto("message", "");

        // when // then
        mvc.perform(post("/api/message")
                        .header("Authorization", getAccessTokenAfterLogIn("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeMessageRequestDto)))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.failMessage").value("수신자를 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("토큰이 있고 수신자가 공백 문자열인 경우 400에러와 수신자를 입력하라는 메서지가 반환된다")
    void writeMessage_Fail_Blank_Receiver() throws Exception{
        // given
        registerMember("receiver");
        WriteMessageRequestDto writeMessageRequestDto = new WriteMessageRequestDto("message", "    ");

        // when // then
        mvc.perform(post("/api/message")
                        .header("Authorization", getAccessTokenAfterLogIn("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeMessageRequestDto)))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.failMessage").value("수신자를 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("토큰이 있고 쪽지 내용이 null인 경우 400에러와 쪽지 내용을 입력하라는 메세지가 반환된다.")
    void writeMessage_Fail_Null_Message() throws Exception{
        // given
        registerMember("receiver");
        WriteMessageRequestDto writeMessageRequestDto = new WriteMessageRequestDto(null, "receiver");

        // when // then
        mvc.perform(post("/api/message")
                        .header("Authorization", getAccessTokenAfterLogIn("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeMessageRequestDto)))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.failMessage").value("쪽지 내용을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("토큰이 있고 쪽지 내용이 빈 칸인 경우 400에러와 쪽지 내용을 입력하라는 메세지가 반환된다.")
    void writeMessage_Fail_Empty_Message() throws Exception{
        // given
        registerMember("receiver");
        WriteMessageRequestDto writeMessageRequestDto = new WriteMessageRequestDto("", "receiver");

        // when // then
        mvc.perform(post("/api/message")
                        .header("Authorization", getAccessTokenAfterLogIn("test"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeMessageRequestDto)))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.result.failMessage").value("쪽지 내용을 입력해주세요."))
                .andDo(print());
    }

    private void registerMember(String text) {
        authService.register(RegisterRequestDto.builder()
                .username(text)
                .nickname(text)
                .email(text + "@" + text + ".com")
                .password(text)
                .passwordCheck(text)
                .build());
    }

    private String getAccessTokenAfterLogIn(String text){
        return authService.logIn(LogInRequestDto.builder().username(text).password(text).build()).getAccessToken();
    }

    private String makeJson(Object object)throws Exception{
        return new ObjectMapper().writeValueAsString(object);
    }
}

