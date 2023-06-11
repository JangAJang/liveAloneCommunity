package com.capstone.liveAloneCommunity.service.member;

import com.capstone.liveAloneCommunity.dto.member.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MemberValidator memberValidator;

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

    @Transactional
    public void changeMemberLocation(ChangeLocationRequestDto changeLocationRequestDto, Member member) {
        Point point = null;
        try {
            point = changeLocationRequestDto.getLatitude() != null
                    && changeLocationRequestDto.getLongitude() != null ?
                    (Point) new WKTReader().read(String.format("POINT(%s %s)", changeLocationRequestDto.getLatitude(),
                            changeLocationRequestDto.getLongitude()))
                    : null;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        member.changePoint(point);
    }

    public MemberResponseDto editNickname(EditNicknameDto editNicknameDto, Member current){
        memberValidator.validateNickname(editNicknameDto.getNickname());
        current.editNickname(editNicknameDto.getNickname());
        return MemberResponseDto.toDto(current);
    }

    public void changePassword(Member member, ChangePasswordRequestDto changePasswordRequestDto){
        memberValidator.validateChangePasswordRequest(member, changePasswordRequestDto);
        member.changePassword(changePasswordRequestDto.getNewPassword(), passwordEncoder);
    }

    public void deleteMember(Member currentMember){
        memberRepository.delete(currentMember);
    }

    private Member findMemberById(Long id){
        return memberRepository.findMemberById(id).orElseThrow(MemberNotFoundException::new);
    }
}
