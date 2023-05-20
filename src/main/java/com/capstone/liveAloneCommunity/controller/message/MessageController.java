package com.capstone.liveAloneCommunity.controller.message;

import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import com.capstone.liveAloneCommunity.dto.message.WriteMessageRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.message.ReadMessageType;
import com.capstone.liveAloneCommunity.repository.message.SearchMessageType;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("")
    @Operation(summary = "단건 쪽지 조회", description = "쪽지를 단건 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readMessage(@RequestParam Long id) {
        return Response.success(messageService.readMessage(id));
    }

    @GetMapping("/receiver")
    @Operation(summary = "받은 쪽지 조회", description = "받은 쪽지를 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readMessageByReceiver(@RequestParam ReadMessageType readMessageType, @PageableDefault Pageable pageable) {
        Member member = getMember();
        MessageSearchRequestDto messageSearchRequestDto = setBuilder(readMessageType, pageable, member);
        return Response.success(messageService.readMessageByCondition(messageSearchRequestDto));
    }

    @GetMapping("/sender")
    @Operation(summary = "보낸 쪽지 조히", description = "보낸 쪽지를 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readMessageBySender(@RequestParam ReadMessageType readMessageType, @PageableDefault Pageable pageable) {
        Member member = getMember();
        MessageSearchRequestDto messageSearchRequestDto = setBuilder(readMessageType, pageable, member);
        return Response.success(messageService.readMessageByCondition(messageSearchRequestDto));
    }

    @GetMapping("/all")
    @Operation(summary = "전체 쪽지 조회", description = "전체 쪽지를 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readMessageAll(@RequestParam ReadMessageType readMessageType, @PageableDefault Pageable pageable) {
        Member member = getMember();
        MessageSearchRequestDto messageSearchRequestDto = setBuilder(readMessageType, pageable, member);
        return Response.success(messageService.readMessageByCondition(messageSearchRequestDto));
    }

    @GetMapping("/search")
    @Operation(summary = "쪽지 검색", description = "쪽지를 검색하여 조회한다.")
    @ResponseStatus(HttpStatus.OK)
    public Response readMessageBySearch(@RequestParam String text, @RequestParam SearchMessageType searchMessageType,
                                        @RequestParam ReadMessageType readMessageType, @PageableDefault Pageable pageable) {
        Member member = getMember();
        MessageSearchRequestDto messageSearchRequestDto = MessageSearchRequestDto.builder()
                .member(member.getNickname())
                .text(text)
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .searchMessageType(searchMessageType)
                .readMessageType(readMessageType)
                .build();
        return Response.success(messageService.readMessageByCondition(messageSearchRequestDto));
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

    private static MessageSearchRequestDto setBuilder(ReadMessageType readMessageType, Pageable pageable, Member member) {
        MessageSearchRequestDto messageSearchRequestDto = MessageSearchRequestDto.builder()
                .member(member.getNickname())
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .readMessageType(readMessageType)
                .build();
        return messageSearchRequestDto;
    }
}
