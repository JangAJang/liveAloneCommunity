package com.capstone.chat.repository.member;

import com.capstone.chat.entity.member.Member;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

@NoRepositoryBean
public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {

    Optional<Member> findByNickname_Nickname(String nickname);
    Mono<Member> findByUsername_Username(String username);
}