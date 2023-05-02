package com.capstone.chat.repository.memberInRoom;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import com.capstone.chat.entity.memberInRoom.MemberInRoom;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MemberInRoomRepository extends ReactiveCrudRepository<MemberInRoom, Long> {

    Flux<MemberInRoom> findMemberInRoomByChatRoom(ChatRoom chatRoom);
    List<MemberInRoom> findByMember(Member member);
    Flux<MemberInRoom> findFluxByMember(Member member);
}
