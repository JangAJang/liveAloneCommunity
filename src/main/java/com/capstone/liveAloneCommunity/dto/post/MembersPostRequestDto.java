package com.capstone.liveAloneCommunity.dto.post;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MembersPostRequestDto {

    private int page;
    private int size;
    private Long id;
}
