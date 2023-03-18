package com.capstone.liveAloneComunity.service;

import com.capstone.liveAloneComunity.dto.member.MemberResponseDto;
import com.capstone.liveAloneComunity.entity.Member;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(Long id) {
        Member member = memberRepository.findMemberById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponseDto.toDto(member);
    }
}
