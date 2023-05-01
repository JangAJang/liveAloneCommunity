package com.capstone.chat.entity.chat;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import jakarta.persistence.Lob;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    private Long id;
    @Lob
    private String message;
    private Long senderId;
    private Long chatRoomId;
    @CreatedDate
    private LocalDateTime sentAt;
}
