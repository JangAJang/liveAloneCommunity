package com.capstone.liveAloneCommunity.controller.message;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.message.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
public class ReadReceiverMessageTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MessageService messageService;

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
}
