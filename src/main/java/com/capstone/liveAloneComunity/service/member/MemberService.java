package com.capstone.liveAloneComunity.service.member;

import com.capstone.liveAloneComunity.dto.member.*;
import com.capstone.liveAloneComunity.entity.member.Member;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Member member = findMemberById(id);
        return MemberResponseDto.toDto(member);
    }

    @Transactional(readOnly = true)
    public MemberSearchResultDto searchMember(SearchMemberDto searchMemberDto, Pageable pageable){
        Page<MemberResponseDto> result = memberRepository.searchMember(searchMemberDto, pageable);
        return new MemberSearchResultDto(result);
    }

    public MemberResponseDto editMember(Long id, EditMemberInfoDto editMemberInfoDto, Member current){
        Member member = findMemberById(id);
        memberValidator.validateEditInfoRequest(editMemberInfoDto);
        member.editInfo(editMemberInfoDto.getNickname(), editMemberInfoDto.getEmail());
        return MemberResponseDto.toDto(member);
    }

    public void changePassword(Long id, Member member, ChangePasswordRequestDto changePasswordRequestDto){
        memberValidator.validateAuthorization(findMemberById(id), member);
        memberValidator.validateChangePasswordRequest(member, changePasswordRequestDto);
        member.changePassword(changePasswordRequestDto.getNewPassword(), passwordEncoder);
    }

    public void deleteMember(Long id, Member currentMember){
        Member target = findMemberById(id);
        memberValidator.validateAuthorization(currentMember, target);
        memberRepository.delete(target);
    }

    private Member findMemberById(Long id){
        return memberRepository.findMemberById(id).orElseThrow(MemberNotFoundException::new);
    }
}
