package com.capstone.liveAloneComunity.service.member;

import com.capstone.liveAloneComunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.dto.member.ChangePasswordRequestDto;
import com.capstone.liveAloneComunity.dto.member.EditMemberInfoDto;
import com.capstone.liveAloneComunity.entity.Member;
import com.capstone.liveAloneComunity.exception.member.*;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateRegister(RegisterRequestDto registerRequestDto){
        if(isUsernamePresent(registerRequestDto.getUsername()))
            throw new UsernameAlreadyInUseException();
        validateNickname(registerRequestDto.getNickname());
        validateEmail(registerRequestDto.getEmail());
        validatePassword(registerRequestDto.getPassword(), registerRequestDto.getPasswordCheck());
    }

    public void validateLogIn(LogInRequestDto logInRequestDto){
        Member member = memberRepository.findByUsername_Username(logInRequestDto.getUsername())
                .orElseThrow(MemberNotFoundException::new);
        if(!member.isRightPassword(logInRequestDto.getPassword(), passwordEncoder))
            throw new PasswordNotMatchingException();
    }

    public void validateEditInfoRequest(EditMemberInfoDto editMemberInfoDto){
        validateNickname(editMemberInfoDto.getNickname());
        validateEmail(editMemberInfoDto.getEmail());
    }

    public void validateChangePasswordRequest(Member member, ChangePasswordRequestDto changePasswordRequestDto){
        validatePassword(changePasswordRequestDto.getNewPassword(), changePasswordRequestDto.getNewPasswordCheck());
        if(!member.isRightPassword(changePasswordRequestDto.getNewPassword(), passwordEncoder))
            throw new CurrentPasswordWrongException();
    }

    public void validateNickname(String nickname){
        if(memberRepository.findByMemberInfo_Nickname(nickname).isPresent())
            throw new NicknameAlreadyInUseException();
    }

    public void validateEmail(String email){
        if(memberRepository.findByMemberInfo_Email(email).isPresent())
            throw new EmailAlreadyInUseException();
        if(isEmailNotFormat(email)) throw new EmailNotFormatException();
    }

    private boolean isEmailNotFormat(String email){
        return !Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", email);
    }

    public boolean isUsernamePresent(String username){
        return memberRepository.findByUsername_Username(username).isPresent();
    }

    public void validatePassword(String password, String passwordCheck){
        if(!password.equals(passwordCheck))
            throw new PasswordNotMatchingException();
    }

    public void validateAuthorization(Member currentMember, Member targetMember){
        if(currentMember.equals(targetMember)) return;
        throw new MemberNotAllowedException();
    }
}
