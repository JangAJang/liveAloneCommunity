package com.capstone.liveAloneComunity.service;

import com.capstone.liveAloneComunity.domain.member.MemberInfo;
import com.capstone.liveAloneComunity.dto.member.EditMemberInfoDto;
import com.capstone.liveAloneComunity.dto.member.MemberResponseDto;
import com.capstone.liveAloneComunity.entity.Member;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private MemberValidator memberValidator;

    @PostConstruct
    private void initiateValidator(){
        memberValidator = new MemberValidator(memberRepository, passwordEncoder);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberInfo(Long id) {
        Member member = findMemberById(id)
        return MemberResponseDto.toDto(member);
    }

    public MemberResponseDto editMember(Long id, EditMemberInfoDto editMemberInfoDto, Member current){
        Member member = findMemberById(id);
        member.editInfo(editMemberInfoDto.getNickname(), editMemberInfoDto.getEmail());
        return MemberResponseDto.toDto(member);
    }

    public void deleteMember(Long id){
        memberRepository.deleteById(id);
    }

    private Member findMemberById(Long id){
        return memberRepository.findMemberById(id).orElseThrow(MemberNotFoundException::new);
    }
}
