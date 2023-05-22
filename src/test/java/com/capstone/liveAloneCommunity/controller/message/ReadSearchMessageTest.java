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
public class ReadSearchMessageTest {

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
    @DisplayName("토큰이 있고 CONTENT 검색 조건으로 올바르게 요청이 들어왔을 때 200코드와 조회된 쪽지들의 정보가 반환된다.")
    void readSearchMessage_Success_SearchByContent() throws Exception{
        // given
        Member sender = getMember("sender");
        Member receiver = getMember("receiver");
        sendMessage(sender, receiver, "apple(S -> R)", 2);
        sendMessage(receiver, sender, "apple(R -> S)", 2);
        sendMessage(sender, receiver, "banana(S -> R)", 2);

        // when // then
        mvc.perform(get("/api/message/search?text=app&page=1&size=10&readMessageType=ALL&searchMessageType=CONTENT")
                        .header("Authorization", getAccessTokenAfterLogIn("sender"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.result[0].content").value("apple(R -> S)1"))
                .andExpect(jsonPath("$.result.data.result[1].content").value("apple(R -> S)0"))
                .andExpect(jsonPath("$.result.data.result[2].content").value("apple(S -> R)1"))
                .andExpect(jsonPath("$.result.data.result[3].content").value("apple(S -> R)0"))
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
