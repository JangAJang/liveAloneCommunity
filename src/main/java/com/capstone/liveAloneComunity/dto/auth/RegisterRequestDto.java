package com.capstone.liveAloneComunity.dto.auth;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class RegisterRequestDto {

    private String username;
    private String nickname;
    private String email;
    private String password;
    private String passwordCheck;
}
