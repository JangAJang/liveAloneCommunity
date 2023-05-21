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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReadReceiverMessageTest {

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
    @DisplayName("토큰이 있고 요청이 올바르게 왔을 경우 200코드와 조회된 쪽지들의 정보가 반환된다.")
    void readReceiverMessage_Success() throws Exception{
        // given
        Member sender = getMember("sender");
        Member receiver = getMember("receiver");
        sendMessage(sender, receiver, "senderToReceiver", 6);
        sendMessage(receiver, sender, "receiverToSender", 3);

        // when // then
        mvc.perform(get("/api/message/receiver?page=0&size=10&readMessageType=RECEIVER")
                        .header("Authorization", getAccessTokenAfterLogIn("sender"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.result[0].content").value("receiverToSender2"))
                .andExpect(jsonPath("$.result.data.result[1].content").value("receiverToSender1"))
                .andExpect(jsonPath("$.result.data.result[2].content").value("receiverToSender0"))
                .andDo(print());
    }

    @Test
    @DisplayName("토큰이 없는 경우 401코드와 다시 로그인하라는 메세지가 반환된다.")
    void readReceiverMessage_Fail_Unauthorized() throws Exception{
        // given
        Member sender = getMember("sender");
        Member receiver = getMember("receiver");
        sendMessage(sender, receiver, "senderToReceiver", 10);
        sendMessage(receiver, sender, "receiverToSender", 10);

        // when // then
        mvc.perform(get("/api/message/receiver")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
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

    private void sendMessage(Member sender, Member receiver, String text, int count) {
        IntStream.range(0, count).forEach(i -> {
            messageService.writeMessage(sender, receiver, text + i);
        });
    }

    private void deleteMessage(Member member, Long id) {
        messageService.deleteMessage(member, id);
    }

    private String getAccessTokenAfterLogIn(String text){
        return authService.logIn(LogInRequestDto.builder().username(text).password(text).build()).getAccessToken();
    }

    private Member getMember(String nickname) {
        return memberRepository.findByNickname_Nickname(nickname).orElseThrow(MemberNotFoundException::new);
    }
}
