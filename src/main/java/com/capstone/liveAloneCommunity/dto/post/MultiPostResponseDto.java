package com.capstone.liveAloneCommunity.dto.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MultiPostResponseDto {

    private List<PostResponseDto> result;
}
