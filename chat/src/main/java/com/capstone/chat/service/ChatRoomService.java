package com.capstone.chat.service;

import com.capstone.chat.dto.chatRoom.MyChatRoomRequestDto;
import com.capstone.chat.dto.chatRoom.SearchChatRoomRequestDto;
import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import com.capstone.chat.exception.member.MemberNotFoundException;
import com.capstone.chat.repository.chatRoom.ChatRoomRepository;
import com.capstone.chat.repository.member.MemberChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberChatRepository memberChatRepository;
    private final ChatRoomRepository chatRoomRepository;

    public Flux<ChatRoom> findMyChatRooms(MyChatRoomRequestDto myChatRoomRequestDto){
        Member member = memberChatRepository.findByNickname(myChatRoomRequestDto.getNickname())
                .orElseThrow(MemberNotFoundException::new);
        return chatRoomRepository.findMembersChatRoom(member.getId());
    }

    public Flux<ChatRoom> searchMyChatRooms(SearchChatRoomRequestDto searchChatRoomRequestDto){
        return findMyChatRooms(new MyChatRoomRequestDto(searchChatRoomRequestDto.getMyName()))
                .filter(i -> i.hasText(searchChatRoomRequestDto.getName()));
    }

    public Mono<ChatRoom> createChatRoom(String sender, String receiver){
        List<String> names = new java.util.ArrayList<>(List.of(sender, receiver));
        Collections.sort(names);
        String roomName = names.get(0) + names.get(1);
        ChatRoom chatRoom = new ChatRoom(roomName);
        return chatRoomRepository.save(chatRoom);
    }
}
