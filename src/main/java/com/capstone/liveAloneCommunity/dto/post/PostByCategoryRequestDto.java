package com.capstone.liveAloneCommunity.dto.post;

import com.capstone.liveAloneCommunity.domain.post.Category;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostByCategoryRequestDto {

    private Category category;
    private int page;
    private int size;
}
