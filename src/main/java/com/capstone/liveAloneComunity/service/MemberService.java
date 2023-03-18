package com.capstone.liveAloneComunity.service;

import com.capstone.liveAloneComunity.domain.member.MemberInfo;
import com.capstone.liveAloneComunity.dto.member.EditMemberInfoDto;
import com.capstone.liveAloneComunity.dto.member.MemberResponseDto;
import com.capstone.liveAloneComunity.entity.Member;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(Long id) {
        Member member = memberRepository.findMemberById(id).orElseThrow(MemberNotFoundException::new);
        return MemberResponseDto.toDto(member);
    }

    public MemberResponseDto editMember(Long id, EditMemberInfoDto editMemberInfoDto){
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        member.editInfo(editMemberInfoDto.getNickname(), editMemberInfoDto.getEmail());
        return MemberResponseDto.toDto(member);
    }

    public void deleteMember(Long id){
        memberRepository.deleteById(id);
    }
}
