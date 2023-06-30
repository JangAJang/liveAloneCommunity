package com.capstone.liveAloneCommunity.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class MultiReadCommentResponseDto {

    private Page<ReadCommentResponseDto> readCommentResponseDto;

    public static MultiReadCommentResponseDto from(final Page<ReadCommentResponseDto> readCommentResponseDto) {
        return new MultiReadCommentResponseDto(readCommentResponseDto);
    }
}
