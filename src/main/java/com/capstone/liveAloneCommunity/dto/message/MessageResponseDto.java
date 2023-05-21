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
    private String createDate;

    private String getCreatedDateToString(LocalDateTime createdDate){
        return createdDate.getYear() + ". " +createdDate.getMonthValue() + ". " + createdDate.getDayOfMonth()
                + ". " + createdDate.getHour() + ":" + createdDate.getMinute();
    }

    @QueryProjection
    public MessageResponseDto(String content, String receiver, String sender, LocalDateTime createDate) {
        this.content = content;
        this.receiver = receiver;
        this.sender = sender;
        this.createDate = getCreatedDateToString(createDate);
    }

    public static MessageResponseDto toDto(Message message) {
        return new MessageResponseDto(
                message.getContent(),
                message.getReceiverNickname(),
                message.getSenderNickname(),
                message.getCreatedDate());
    }
}
