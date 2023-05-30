package com.capstone.liveAloneCommunity.dto.post;

import com.capstone.liveAloneCommunity.repository.post.SearchPostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class SearchPostRequestDto {

    private int page;
    private int size;
    @NotBlank(message = "검색할 내용을 입력해주세요.")
    private String text;
    private SearchPostType searchPostType;
}
