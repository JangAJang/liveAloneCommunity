package com.capstone.chat.repository.memberInRoom;

import com.capstone.chat.entity.chatRoom.ChatRoom;
import com.capstone.chat.entity.member.Member;
import com.capstone.chat.entity.memberInRoom.MemberInRoom;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface MemberInRoomRepository extends ReactiveCrudRepository<MemberInRoom, Long> {

    Flux<MemberInRoom> findMemberInRoomByChatRoomId(Long chatRoomId);
    List<MemberInRoom> findByMemberId(Member member);
    Flux<MemberInRoom> findFluxByMemberId(Long memberId);
}
