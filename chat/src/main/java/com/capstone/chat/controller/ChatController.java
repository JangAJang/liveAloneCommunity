package com.capstone.chat.controller;

import com.capstone.chat.dto.chat.ChatResponseDto;
import com.capstone.chat.dto.chat.IntoRoomRequestDto;
import com.capstone.chat.dto.chat.SendChatRequestDto;
import com.capstone.chat.dto.chatRoom.CreateRoomRequestDto;
import com.capstone.chat.dto.chatRoom.MyChatRoomRequestDto;
import com.capstone.chat.dto.chatRoom.SearchChatRoomRequestDto;
import com.capstone.chat.entity.chat.Chat;
import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.service.ChatRoomService;
import com.capstone.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @CrossOrigin
    @PostMapping(value = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<Chat> sendMessage(@RequestBody @Validated SendChatRequestDto sendChatRequestDto){
        return chatService.sendMessage(sendChatRequestDto);
    }

    @CrossOrigin
    @GetMapping(value = "/rooms", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatRoom> getMyRooms(@RequestBody @Validated MyChatRoomRequestDto myChatRoomRequestDto){
        return chatRoomService.findMyChatRooms(myChatRoomRequestDto);
    }

    @CrossOrigin
    @GetMapping(value = "/rooms/search", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatRoom> searchRooms(@RequestBody @Validated SearchChatRoomRequestDto searchChatRoomRequestDto){
        return chatRoomService.searchMyChatRooms(searchChatRoomRequestDto);
    }

    @CrossOrigin
    @PostMapping(value = "/rooms/create", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Mono<ChatRoom> createRoom(@RequestBody @Validated CreateRoomRequestDto createRoomRequestDto){
        return chatRoomService.createChatRoom(createRoomRequestDto.getSender(), createRoomRequestDto.getReceiver());
    }

    @CrossOrigin
    @GetMapping(value = "/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponseDto> getInRoom(@RequestBody @Validated IntoRoomRequestDto intoRoomRequestDto){
        return chatService.getIntoRoom(intoRoomRequestDto);
    }
}
