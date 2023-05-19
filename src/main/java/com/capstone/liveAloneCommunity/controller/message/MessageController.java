package com.capstone.liveAloneCommunity.controller.message;

import com.capstone.liveAloneCommunity.dto.message.WriteMessageRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/message")
public class MessageController {

    private final MemberRepository memberRepository;
    private final MessageService messageService;

    @PostMapping("")
    @Operation(summary = "쪽지 작성", description = "쪽지를 작성한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response writeMessage(@RequestBody @Valid WriteMessageRequestDto writeMessageRequestDto) {
        Member member = getMember();
        Member receiver = getReceiver(writeMessageRequestDto.getReceiver());
        return Response.success(messageService.writeMessage(member, receiver, writeMessageRequestDto));
    }

    private Member getMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return memberRepository.findByUsername_Username(authentication.getName())
                .orElseThrow(MemberNotFoundException::new);
    }

    private Member getReceiver(String receiver) {
        return memberRepository.findByNickname_Nickname(receiver)
                .orElseThrow(MemberNotFoundException::new);
    }
}
