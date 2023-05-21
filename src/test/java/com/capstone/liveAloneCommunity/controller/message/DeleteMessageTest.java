package com.capstone.liveAloneCommunity.controller.message;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.message.MessageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteMessageTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void initData() {
        registerMember("sender");
        registerMember("receiver");
    }

    @AfterEach
    void clearDB() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("토큰이 있고 요청이 올바르게 왔을 경우 200코드와 쪽지 삭제 완료 메세지가 반환된다.")
    void deleteMessage_Success() throws Exception{
        // given
        Member sender = getMember("sender");
        Member receiver = getMember("receiver");
        sendMessage(sender, receiver, 1);

        // when // then
        mvc.perform(delete("/api/message?id=1")
                        .header("Authorization", getAccessTokenAfterLogIn("sender"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value("쪽지 삭제 완료"))
                .andDo(print());
    }

    @Test
    @DisplayName("토큰이 있고 요청하는 회원이 삭제한 쪽지를 다시 삭제하려는 경우 404코드와 쪽지를 찾을 수 없다는 메세지가 반환된다.")
    void deleteMessage_Fail_DeletedMessage() throws Exception{
        // given
        Member sender = getMember("sender");
        Member receiver = getMember("receiver");
        sendMessage(sender, receiver, 1);
        messageService.deleteMessage(sender, 1L);

        // when // then
        mvc.perform(delete("/api/message?id=1")
                        .header("Authorization", getAccessTokenAfterLogIn("sender"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.result.failMessage").value("해당 쪽지를 찾을 수 없습니다."))
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

    private void sendMessage(Member sender, Member receiver, int count) {
        IntStream.range(0, count).forEach(i -> {
            messageService.writeMessage(sender, receiver, "message" + i);
        });
    }

    private Member getMember(String nickname) {
        return memberRepository.findByNickname_Nickname(nickname).orElseThrow(MemberNotFoundException::new);
    }

    private String getAccessTokenAfterLogIn(String text){
        return authService.logIn(LogInRequestDto.builder().username(text).password(text).build()).getAccessToken();
    }
}
