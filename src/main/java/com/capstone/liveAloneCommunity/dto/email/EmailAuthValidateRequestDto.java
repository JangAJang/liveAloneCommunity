package com.capstone.liveAloneCommunity.dto.email;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmailAuthValidateRequestDto {

    private String email;
    private String authNum;
}
