package com.capstone.chat.service;

import com.capstone.chat.dto.chat.SendChatRequestDto;
import com.capstone.chat.entity.chat.Chat;
import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import com.capstone.chat.entity.memberInRoom.MemberInRoom;
import com.capstone.chat.exception.member.MemberNotFoundException;
import com.capstone.chat.repository.chat.ChatRepository;
import com.capstone.chat.repository.member.MemberRepository;
import com.capstone.chat.repository.memberInRoom.MemberInRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final MemberInRoomRepository memberInRoomRepository;

    public Mono<Chat> sendMessage(SendChatRequestDto sendChatRequestDto){
        Member sender = memberRepository.findByNickname_Nickname(sendChatRequestDto.getSenderName()).orElseThrow(MemberNotFoundException::new);
        Member receiver = memberRepository.findByNickname_Nickname(sendChatRequestDto.getReceiverName()).orElseThrow(MemberNotFoundException::new);
        ChatRoom chatRoom = findChatRoomInCommon(sender, receiver);
        if(chatRoom == null){
            String roomName = createRoomNameByMembers(sender, receiver);
            chatRoom = new ChatRoom(roomName);
        }
        Chat chat = Chat.builder()
                .message(sendChatRequestDto.getMessage())
                .sender(sender)
                .chatRoom(chatRoom)
                .sentAt(LocalDateTime.now())
                .build();
        return chatRepository.save(chat);
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
        for(MemberInRoom receiverRoomEach : receiverRooms){
            if(senderRooms.stream().map(MemberInRoom::getChatRoom).toList().contains(receiverRoomEach)){
                return receiverRoomEach.getChatRoom();
            }
        }
        return null;
    }
}
