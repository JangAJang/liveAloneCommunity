package com.capstone.liveAloneCommunity.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class EditNicknameDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;
}
