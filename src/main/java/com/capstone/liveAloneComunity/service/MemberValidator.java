package com.capstone.liveAloneComunity.service;

import com.capstone.liveAloneComunity.dto.member.RegisterRequestDto;
import com.capstone.liveAloneComunity.exception.member.*;
import com.capstone.liveAloneComunity.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateRegister(RegisterRequestDto registerRequestDto){
        if(isUsernamePresent(registerRequestDto.getUsername()))
            throw new UsernameAlreadyInUseException();
        validateNickname(registerRequestDto.getNickname());
        validateEmail(registerRequestDto.getEmail());
        validatePassword(registerRequestDto.getPassword(), registerRequestDto.getPasswordCheck());
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
}
