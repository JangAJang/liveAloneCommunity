package com.capstone.chat.dto.chat;

import com.capstone.chat.entity.chat.Chat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatResponseDto {

    private String sender;
    private String receiver;
    private String message;
    private String sentDate;

    public ChatResponseDto(Chat chat) {
        sender = chat.getSenderName();
        message = chat.getMessage();
        sentDate = chat.getSentAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static ChatResponseDto toDto(Chat chat){
        return new ChatResponseDto(chat);
    }

}
