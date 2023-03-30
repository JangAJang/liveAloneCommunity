package com.capstone.liveAloneCommunity.service.auth;
import com.capstone.liveAloneCommunity.config.jwt.TokenProvider;
import com.capstone.liveAloneCommunity.dto.auth.*;
import com.capstone.liveAloneCommunity.dto.token.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.token.RefreshToken;
import com.capstone.liveAloneCommunity.exception.authentication.NeedToLoginException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.refreshToken.RefreshTokenRepository;
import com.capstone.liveAloneCommunity.service.member.MemberValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private TokenValidator tokenValidator;
    private MemberValidator memberValidator;

    @PostConstruct
    private void initiateValidator(){
        memberValidator = new MemberValidator(memberRepository, passwordEncoder);
        tokenValidator = new TokenValidator(tokenProvider);
    }

    public void register(RegisterRequestDto registerRequestDto){
        memberValidator.validateRegister(registerRequestDto);
        memberRepository.save(new Member(registerRequestDto, passwordEncoder));
    }

    public TokenResponseDto logIn(LogInRequestDto logInRequestDto){
        memberValidator.validateLogIn(logInRequestDto);
        return createTokenDtoByAuthentication(getAuthenticationToLogIn(logInRequestDto));
    }

    public TokenResponseDto reissue(ReissueRequestDto reissueRequestDto){
        reissueRequestDto.deletePrefix();
        RefreshToken refreshToken = refreshTokenRepository.findByKey(SecurityContextHolder.getContext().getAuthentication().getName())
                        .orElseThrow(NeedToLoginException::new);
        tokenValidator.validateRefreshToken(refreshToken);
        Authentication tokenAuthentication = tokenValidator.getAuthentication(reissueRequestDto);
        TokenDto tokenDto = tokenProvider.generateTokenDto(tokenAuthentication);
        refreshToken.updateValue(tokenDto.getRefreshToken());
        return new TokenResponseDto(tokenDto);
    }

    private Authentication getAuthenticationToLogIn(LogInRequestDto logInRequestDto){
        UsernamePasswordAuthenticationToken authenticationToken = logInRequestDto.toAuthentication();
        return authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    }

    private TokenResponseDto createTokenDtoByAuthentication(Authentication authentication){
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);
        return new TokenResponseDto(tokenDto);
    }
}
