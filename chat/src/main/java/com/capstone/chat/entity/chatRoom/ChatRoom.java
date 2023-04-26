package com.capstone.chat.entity.chatRoom;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    private Long id;
    private String name;

    public ChatRoom(String name) {
        this.name = name;
    }

    public boolean hasText(String text){
        return name.contains(text);
    }
}
