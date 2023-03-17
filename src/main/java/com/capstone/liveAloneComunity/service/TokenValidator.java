package com.capstone.liveAloneComunity.service;

import com.capstone.liveAloneComunity.config.jwt.TokenProvider;
import com.capstone.liveAloneComunity.dto.token.ReissueRequestDto;
import com.capstone.liveAloneComunity.entity.RefreshToken;
import com.capstone.liveAloneComunity.exception.token.TokenUnmatchWithMemberException;
import com.capstone.liveAloneComunity.exception.token.UnvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class TokenValidator {

    private final TokenProvider tokenProvider;

    public void validateRefreshToken(ReissueRequestDto reissueRequestDto){
        if (!tokenProvider.validateToken(reissueRequestDto.getRefreshToken())) {
            throw new UnvalidRefreshTokenException();
        }
    }

    public void validateTokenInfo(RefreshToken refreshToken, ReissueRequestDto reissueRequestDto){
        if (!refreshToken.getValue().equals(reissueRequestDto.getRefreshToken())) {
            throw new TokenUnmatchWithMemberException();
        }
    }

    public Authentication getAuthentication(ReissueRequestDto reissueRequestDto){
        return tokenProvider.getAuthentication(reissueRequestDto.getAccessToken());
    }
}
