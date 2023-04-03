package com.capstone.liveAloneCommunity.dto.email;

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
public class EmailAuthValidateRequestDto {

    @NotNull(message = "이메일을 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
    @NotNull(message = "인증번호를 입력해주세요.")
    @NotBlank(message = "인증번호를 입력해주세요.")
    @NotEmpty(message = "인증번호를 입력해주세요.")
    private String authNum;
}
