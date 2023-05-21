package com.capstone.liveAloneCommunity.controller.message;

import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

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
