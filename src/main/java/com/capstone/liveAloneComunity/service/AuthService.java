package com.capstone.liveAloneComunity.service;
import com.capstone.liveAloneComunity.dto.member.RegisterRequestDto;
import com.capstone.liveAloneComunity.entity.Member;
import com.capstone.liveAloneComunity.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
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
    private MemberValidator memberValidator;

    @PostConstruct
    private void initiateValidator(){
        memberValidator = new MemberValidator(memberRepository);
    }

    public void register(RegisterRequestDto registerRequestDto){
        memberValidator.validateRegister(registerRequestDto);
        memberRepository.save(new Member(registerRequestDto, passwordEncoder));
    }
}
