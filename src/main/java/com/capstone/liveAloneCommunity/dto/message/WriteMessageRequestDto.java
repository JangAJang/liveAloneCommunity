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

    @NotBlank
    private String content;
    @NotBlank
    private String receiver;
}
