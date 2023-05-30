package com.capstone.liveAloneCommunity.repository.member;

import com.capstone.liveAloneCommunity.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    Optional<Member> findByUsername_Username(String username);
    Optional<Member> findByNickname_Nickname(String nickname);
    Optional<Member> findByEmail_Email(String email);
    Optional<Member> findMemberById(Long id);
}
