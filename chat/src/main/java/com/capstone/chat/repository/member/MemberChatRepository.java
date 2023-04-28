package com.capstone.chat.repository.member;

import com.capstone.chat.entity.member.Member;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface MemberChatRepository extends ReactiveCrudRepository<Member, Long>{

    Optional<Member> findByNickname(String nickname);
    Mono<Member> findByUsername(String username);
}
