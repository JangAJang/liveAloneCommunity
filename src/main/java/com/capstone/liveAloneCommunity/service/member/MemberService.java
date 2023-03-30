package com.capstone.liveAloneCommunity.service.member;

import com.capstone.liveAloneCommunity.dto.member.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public MemberSearchResultDto searchMember(SearchMemberDto searchMemberDto){
        Page<MemberResponseDto> result = memberRepository.searchMember(searchMemberDto);
        return new MemberSearchResultDto(result);
    }

    public MemberResponseDto editMember(Long id, EditMemberInfoDto editMemberInfoDto, Member current){
        Member member = findMemberById(id);
        memberValidator.validateAuthorization(current, member);
        memberValidator.validateEditInfoRequest(editMemberInfoDto);
        member.editInfo(editMemberInfoDto.getNickname(), editMemberInfoDto.getEmail());
        return MemberResponseDto.toDto(member);
    }

    public void changePassword(Member member, ChangePasswordRequestDto changePasswordRequestDto){
        memberValidator.validateAuthorization(findMemberById(changePasswordRequestDto.getId()), member);
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
