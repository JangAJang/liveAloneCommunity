package com.capstone.chat.dto.chat;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SendChatRequestDto {
    private String senderName;
    private String message;
    private String receiverName;
}
