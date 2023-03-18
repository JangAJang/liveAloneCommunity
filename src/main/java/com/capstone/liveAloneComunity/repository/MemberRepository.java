package com.capstone.liveAloneComunity.repository;

import com.capstone.liveAloneComunity.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername_Username(String username);
    Optional<Member> findByMemberInfo_Nickname(String nickname);
    Optional<Member> findByMemberInfo_Email(String email);
    Optional<Member> findMemberById(Long id);
}
