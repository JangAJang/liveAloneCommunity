package com.capstone.liveAloneComunity.service;

import com.capstone.liveAloneComunity.domain.member.Username;
import com.capstone.liveAloneComunity.dto.member.RegisterRequestDto;
import com.capstone.liveAloneComunity.entity.Member;
import com.capstone.liveAloneComunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequestDto registerRequestDto){
        memberRepository.save(new Member(registerRequestDto, passwordEncoder));
    }
}
