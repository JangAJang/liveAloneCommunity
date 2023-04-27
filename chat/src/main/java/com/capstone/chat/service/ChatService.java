package com.capstone.chat.service;

import com.capstone.chat.dto.chat.*;
import com.capstone.chat.entity.chat.Chat;
import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import com.capstone.chat.entity.memberInRoom.MemberInRoom;
import com.capstone.chat.exception.member.MemberNotFoundException;
import com.capstone.chat.repository.chat.ChatRepository;
import com.capstone.chat.repository.chatRoom.ChatRoomRepository;
import com.capstone.chat.repository.member.MemberRepository;
import com.capstone.chat.repository.memberInRoom.MemberInRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.*;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberInRoomRepository memberInRoomRepository;

    public Mono<ChatResponseDto> sendMessage(SendChatRequestDto sendChatRequestDto){
        Member sender = memberRepository.findByNickname(sendChatRequestDto.getSenderName()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByNickname(sendChatRequestDto.getReceiverName()).orElseThrow(MemberNotFoundException::new);
        ChatRoom chatRoom = findChatRoomInCommon(sender, receiver);
        if(chatRoom == null){
            String roomName = createRoomNameByMembers(sender, receiver);
            chatRoom = new ChatRoom(roomName);
            memberInRoomRepository.save(new MemberInRoom(chatRoom, sender));
            memberInRoomRepository.save(new MemberInRoom(chatRoom, receiver));
        }
        Chat chat = Chat.builder()
                .message(sendChatRequestDto.getMessage())
                .sender(sender)
                .chatRoom(chatRoom)
                .build();
        return chatRepository.save(chat).map(ChatResponseDto::toDto);
    }

    public Flux<ChatResponseDto> getIntoRoom(IntoRoomRequestDto intoRoomRequestDto){
        Mono<ChatRoom> chatRoom = chatRoomRepository.findById(intoRoomRequestDto.getRoomId());
        return chatRepository.findByChatRoomOrderBySentAtAsc(chatRoom.block()).map(ChatResponseDto::toDto);
    }

    private String createRoomNameByMembers(Member sender, Member receiver){
        List<String> names = new ArrayList<>();
        names.add(sender.getNickname());
        names.add(receiver.getNickname());
        Collections.sort(names);
        return names.get(0) + ", " + names.get(1);
    }

    private ChatRoom findChatRoomInCommon(Member sender, Member receiver){
        List<MemberInRoom> senderRooms = memberInRoomRepository.findByMember(sender);
        List<MemberInRoom> receiverRooms = memberInRoomRepository.findByMember(receiver);
        for(MemberInRoom receiverRoom : receiverRooms){
            if(senderRooms.contains(receiverRoom)) return receiverRoom.getChatRoom();
        }
        return null;
    }
}
