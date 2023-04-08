package com.capstone.liveAloneCommunity.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class ChangePasswordRequestDto {

    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    private String currentPassword;
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;
    @NotBlank(message = "새 비밀번호를 다시 입력해주세요.")
    private String newPasswordCheck;
}
