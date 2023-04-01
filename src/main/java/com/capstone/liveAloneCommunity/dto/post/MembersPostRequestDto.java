package com.capstone.liveAloneCommunity.dto.post;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class MembersPostRequestDto {

    private int page;
    private int size;
    private Long id;
}
