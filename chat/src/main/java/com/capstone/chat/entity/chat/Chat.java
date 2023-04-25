package com.capstone.chat.entity.chat;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id
    private Long id;
    @Lob
    private String msg;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;
    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;
    private LocalDateTime sentAt;
}
