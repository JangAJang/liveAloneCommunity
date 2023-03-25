package com.capstone.liveAloneCommunity.service.auth;

import com.capstone.liveAloneCommunity.config.jwt.TokenProvider;
import com.capstone.liveAloneCommunity.dto.token.ReissueRequestDto;
import com.capstone.liveAloneCommunity.entity.RefreshToken;
import com.capstone.liveAloneCommunity.exception.token.TokenUnmatchWithMemberException;
import com.capstone.liveAloneCommunity.exception.token.UnvalidRefreshTokenException;
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
