package com.capstone.liveAloneComunity.dto.token;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;

    public TokenResponseDto(TokenDto tokenDto){
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
    }
}