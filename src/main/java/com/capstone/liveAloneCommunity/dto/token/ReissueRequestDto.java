package com.capstone.liveAloneCommunity.dto.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReissueRequestDto {
    private String accessToken;

    public void deletePrefix() {
        accessToken = accessToken.substring(7);
        System.out.println(accessToken);
    }
}
