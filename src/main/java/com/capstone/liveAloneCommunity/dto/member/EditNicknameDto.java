package com.capstone.liveAloneCommunity.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class EditNicknameDto {
    @NotBlank(message = "닉네임을 입력해주세요.")
    @NotEmpty(message = "닉네임을 입력해주세요.")
    @NotNull(message = "닉네임을 입력해주세요.")
    private String nickname;
}
