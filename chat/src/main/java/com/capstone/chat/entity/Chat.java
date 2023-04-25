package com.capstone.chat.entity;

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
    private String sender;
    private String receiver;
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;
    private LocalDateTime sentAt;
}
