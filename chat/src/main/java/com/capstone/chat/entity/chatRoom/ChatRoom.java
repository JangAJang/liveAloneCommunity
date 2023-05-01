package com.capstone.chat.entity.chatRoom;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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
