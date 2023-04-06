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
public class EmailAuthRequestDto {

    @NotBlank(message = "이메일을 입력해주세요.")
    @NotNull(message = "이메일을 입력해주세요.")
    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;
}
