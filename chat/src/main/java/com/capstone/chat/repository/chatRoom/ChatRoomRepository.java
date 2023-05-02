package com.capstone.chat.repository.chatRoom;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, Long> {

    @Query("select * from chat_room c where c.id in (select m.chat_room_id from member_in_room m where m.member_id = :memberId)")
    Flux<ChatRoom> findMembersChatRoom(@Param("memberId") Long memberId);
    @Query("select m from member_in_room m where m.member_id = :senderId and m.chat_room_id in (" +
            "select n.chat_room_id from member_in_room n where n.member_id = :receiverId")
    Mono<ChatRoom> findChatRoomBySenderAndReceiver(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);

}
