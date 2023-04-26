package com.capstone.chat.entity.chat;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Chat {

    @Id
    private Long id;
    @Lob
    private String message;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;
    private LocalDateTime sentAt;

    public String getSenderName(){
        return this.sender.getNickname();
    }
}
