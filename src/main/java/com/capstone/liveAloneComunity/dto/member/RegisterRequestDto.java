package com.capstone.liveAloneComunity.dto.member;

import lombok.*;

@RequiredArgsConstructor
@Builder
@Getter
public class RegisterRequestDto {

    private final String username;
    private final String nickname;
    private final String email;
    private final String password;
    private final String passwordCheck;
}
