package com.capstone.liveAloneCommunity.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class RegisterRequestDto {
    @NotNull(message = "아이디를 입력해주세요.")
    @NotBlank(message = "아이디를 입력해주세요.")
    @NotEmpty(message = "아이디를 입력해주세요.")
    private String username;
    @NotNull(message = "닉네임을 입력해주세요.")
    @NotBlank(message = "닉네임을 입력해주세요.")
    @NotEmpty(message = "닉네임을 입력해주세요.")
    private String nickname;
    @NotNull(message = "이메일을 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotNull(message = "비밀번호를 입력해주세요.")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotNull(message = "비밀번호를 다시 입력해주세요.")
    @NotBlank(message = "비밀번호를 다시 입력해주세요.")
    @NotEmpty(message = "비밀번호를 다시 입력해주세요.")
    private String passwordCheck;
}
