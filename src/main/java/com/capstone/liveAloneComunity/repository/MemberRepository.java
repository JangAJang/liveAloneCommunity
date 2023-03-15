package com.capstone.liveAloneComunity.repository;

import com.capstone.liveAloneComunity.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);
}
