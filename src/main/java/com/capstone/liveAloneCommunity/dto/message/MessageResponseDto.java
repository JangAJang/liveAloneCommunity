package com.capstone.liveAloneCommunity.dto.message;

import com.capstone.liveAloneCommunity.entity.message.Message;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class MessageResponseDto {

    private String content;
    private String receiver;
    private String sender;
    private LocalDateTime createDate;

    @QueryProjection
    public MessageResponseDto(String content, String receiver, String sender, LocalDateTime createDate) {
        this.content = content;
        this.receiver = receiver;
        this.sender = sender;
        this.createDate = createDate;
    }

    public static MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getContent(),
                message.getReceiverNickname(),
                message.getSenderNickname(),
                message.getCreatedDate());
    }
}
