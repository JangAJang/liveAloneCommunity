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
    private Long chatRoomId;
    private Long memberId;

    public MemberInRoom(ChatRoom chatRoom, Member member) {
        this.chatRoomId = chatRoom.getId();
        this.memberId = member.getId();
    }
}
