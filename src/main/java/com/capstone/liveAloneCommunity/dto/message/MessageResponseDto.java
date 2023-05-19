package com.capstone.liveAloneCommunity.dto.message;

import com.capstone.liveAloneCommunity.entity.message.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MessageResponseDto {

    private String content;
    private String receiver;
    private String sender;
    private LocalDateTime createDate;

    public static MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getContent(),
                message.getReceiverNickname(),
                message.getSenderNickname(),
                message.getCreatedDate());
    }
}
