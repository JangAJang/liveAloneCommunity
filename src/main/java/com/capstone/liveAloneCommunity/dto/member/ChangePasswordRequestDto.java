package com.capstone.liveAloneCommunity.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangePasswordRequestDto {

    private Long id;
    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    @NotEmpty(message = "기존 비밀번호를 입력해주세요.")
    @NotNull(message = "기존 비밀번호를 입력해주세요.")
    private String currentPassword;
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    @NotEmpty(message = "새 비밀번호를 입력해주세요.")
    @NotNull(message = "새 비밀번호를 입력해주세요.")
    private String newPassword;
    @NotBlank(message = "새 비밀번호를 다시 입력해주세요.")
    @NotEmpty(message = "새 비밀번호를 다시 입력해주세요.")
    @NotNull(message = "새 비밀번호를 다시 입력해주세요.")
    private String newPasswordCheck;
}
