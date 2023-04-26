package com.capstone.chat.dto.chat;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IntoRoomRequestDto {

    private String senderName;
    private Long roomId;
}
