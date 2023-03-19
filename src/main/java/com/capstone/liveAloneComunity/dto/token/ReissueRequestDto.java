package com.capstone.liveAloneComunity.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReissueRequestDto {

    private String refreshToken;

    private String accessToken;

    public void deletePrefix(){
        refreshToken = refreshToken.substring(7);
        System.out.println(refreshToken);
        accessToken = accessToken.substring(7);
        System.out.println(accessToken);
    }
}
