package com.capstone.liveAloneCommunity.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangePasswordRequestDto {

    private Long id;
    private String currentPassword;
    private String newPassword;
    private String newPasswordCheck;
}
