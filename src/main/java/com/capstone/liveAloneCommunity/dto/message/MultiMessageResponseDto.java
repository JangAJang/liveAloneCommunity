package com.capstone.liveAloneCommunity.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultiMessageResponseDto {

    List<MessageResponseDto> result;
}
