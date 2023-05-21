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
public class ReadSingleMessageTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MessageService messageService;
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
    @DisplayName("토큰이 있고 요청한 쪽지가 자신이 보내거나 받은 쪽지인 경우 200코드와 조회된 쪽지 정보가 반환된다.")
    void readSingleMessage_Success() throws Exception{
        // given
        Member sender = getMember("sender");
        Member receiver = getMember("receiver");
        sendMessage(sender, receiver, 1);

        // when // then
        mvc.perform(get("/api/message?id=1")
                        .header("Authorization", getAccessTokenAfterLogIn("sender"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.content").value("message0"))
                .andExpect(jsonPath("$.result.data.receiver").value("receiver"))
                .andExpect(jsonPath("$.result.data.sender").value("sender"))
                .andExpect(jsonPath("$.result.data.createDate").isString())
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

    private String getAccessTokenAfterLogIn(String text){
        return authService.logIn(LogInRequestDto.builder().username(text).password(text).build()).getAccessToken();
    }

    private Member getMember(String nickname) {
        return memberRepository.findByNickname_Nickname(nickname).orElseThrow(MemberNotFoundException::new);
    }
}
