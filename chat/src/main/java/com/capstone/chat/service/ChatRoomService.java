package com.capstone.chat.service;

import com.capstone.chat.dto.chatRoom.MyChatRoomRequestDto;
import com.capstone.chat.dto.chatRoom.SearchChatRoomRequestDto;
import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import com.capstone.chat.entity.memberInRoom.MemberInRoom;
import com.capstone.chat.exception.member.MemberNotFoundException;
import com.capstone.chat.repository.chatRoom.ChatRoomRepository;
import com.capstone.chat.repository.member.MemberRepository;
import com.capstone.chat.repository.memberInRoom.MemberInRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final MemberInRoomRepository memberInRoomRepository;
    private final MemberRepository memberRepository;

    public Flux<ChatRoom> findMyChatRooms(MyChatRoomRequestDto myChatRoomRequestDto){
        Member member = memberRepository.findByNickname_Nickname(myChatRoomRequestDto.getNickname())
                .orElseThrow(MemberNotFoundException::new);
        Flux<MemberInRoom> myRooms = memberInRoomRepository.findFluxByMember(member);
        return myRooms.map(MemberInRoom::getChatRoom);
    }

    public Flux<ChatRoom> searchMyChatRooms(SearchChatRoomRequestDto searchChatRoomRequestDto){
        return findMyChatRooms(new MyChatRoomRequestDto(searchChatRoomRequestDto.getMyName()))
                .filter(i -> i.hasText(searchChatRoomRequestDto.getName()));
    }
}
