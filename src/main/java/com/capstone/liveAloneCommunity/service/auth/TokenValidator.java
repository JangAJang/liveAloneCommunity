package com.capstone.liveAloneCommunity.service.auth;

import com.capstone.liveAloneCommunity.config.jwt.TokenProvider;
import com.capstone.liveAloneCommunity.dto.token.ReissueRequestDto;
import com.capstone.liveAloneCommunity.entity.token.RefreshToken;
import com.capstone.liveAloneCommunity.exception.token.UnvalidRefreshTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

@RequiredArgsConstructor
public class TokenValidator {

    private final TokenProvider tokenProvider;

    public void validateRefreshToken(RefreshToken refreshToken){
        if (!tokenProvider.validateToken(refreshToken.getValue())) {
            throw new UnvalidRefreshTokenException();
        }
    }

    public Authentication getAuthentication(ReissueRequestDto reissueRequestDto){
        return tokenProvider.getAuthentication(reissueRequestDto.getAccessToken());
    }
}
