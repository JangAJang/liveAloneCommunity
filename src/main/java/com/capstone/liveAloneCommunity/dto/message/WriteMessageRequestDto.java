package com.capstone.liveAloneCommunity.dto.message;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WriteMessageRequestDto {

    @NotBlank(message = "쪽지 내용을 입력해주세요.")
    private String content;
    @NotBlank(message = "수신자를 입력해주세요.")
    private String receiver;
}
