package com.capstone.chat.repository.member;

import com.capstone.chat.entity.member.Member;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface MemberRepository extends ReactiveCrudRepository<Member, Long> {

    Mono<Member> findByNickname_Nickname(String nickname);
    Mono<Member> findByUsername_Username(String username);
}
