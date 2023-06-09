package com.capstone.liveAloneCommunity.dto.token;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {

    private static final String BEARER_PREFIX = "Bearer ";

    private String accessToken;

    public TokenResponseDto(TokenDto tokenDto){
        this.accessToken = BEARER_PREFIX + tokenDto.getAccessToken();
    }
}