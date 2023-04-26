package com.capstone.chat.repository.memberInRoom;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.memberInRoom.MemberInRoom;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface MemberInRoomRepository extends ReactiveCrudRepository<MemberInRoom, Long> {

    Flux<MemberInRoom> findMemberInRoomByChatRoom(ChatRoom chatRoom);
}
