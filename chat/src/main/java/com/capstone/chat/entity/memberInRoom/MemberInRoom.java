package com.capstone.chat.entity.memberInRoom;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInRoom {

    @Id
    private Long id;
    private ChatRoom chatRoom;
    private Member member;

    public MemberInRoom(ChatRoom chatRoom, Member member) {
        this.chatRoom = chatRoom;
        this.member = member;
    }
}
