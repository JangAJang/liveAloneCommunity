package com.capstone.liveAloneCommunity.dto.post;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class EditPostRequestDto {

    private Long id;
    private String title;
    private String content;
}
