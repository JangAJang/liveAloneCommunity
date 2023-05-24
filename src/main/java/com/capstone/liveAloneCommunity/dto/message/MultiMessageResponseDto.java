package com.capstone.liveAloneCommunity.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Getter
public class MultiMessageResponseDto {

    Page<MessageResponseDto> result;
}
