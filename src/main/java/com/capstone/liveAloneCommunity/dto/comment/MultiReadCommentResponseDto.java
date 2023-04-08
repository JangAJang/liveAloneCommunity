package com.capstone.liveAloneCommunity.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MultiReadCommentResponseDto {

    private List<ReadCommentResponseDto> readCommentResponseDto;
}
