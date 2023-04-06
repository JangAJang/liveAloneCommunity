package com.capstone.liveAloneCommunity.dto.email;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(staticName = "toDto")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class EmailAuthResponseDto {

    private String authNum;
}
