package com.capstone.chat.service;

import com.capstone.chat.dto.chat.*;
import com.capstone.chat.entity.chat.Chat;
import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import com.capstone.chat.exception.member.MemberNotFoundException;
import com.capstone.chat.repository.chat.ChatRepository;
import com.capstone.chat.repository.chatRoom.ChatRoomRepository;
import com.capstone.chat.repository.member.MemberChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;
@RequiredArgsConstructor
@Service
public class ChatService {

    private final MemberChatRepository memberChatRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Mono<Chat> sendMessage(SendChatRequestDto sendChatRequestDto){
        Member sender = memberChatRepository.findByNickname(sendChatRequestDto.getSenderName()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberChatRepository.findByNickname(sendChatRequestDto.getReceiverName()).orElseThrow(MemberNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findChatRoomBySenderAndReceiver(sender.getId(), receiver.getId())
                .orElseThrow(IllegalArgumentException::new);
        Chat chat = Chat.builder()
                .message(sendChatRequestDto.getMessage())
                .chatRoomId(chatRoom.getId())
                .senderId(sender.getId())
                .build();
        return chatRepository.save(chat);
    }

    public Flux<ChatResponseDto> getIntoRoom(IntoRoomRequestDto intoRoomRequestDto){
        return chatRepository.findByChatRoomIdOrderBySentAtDesc(intoRoomRequestDto.getRoomId())
                .map(ChatResponseDto::toDto);
    }
}
