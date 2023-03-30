package com.capstone.liveAloneCommunity.dto.post;

import com.capstone.liveAloneCommunity.repository.post.SearchPostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class SearchPostRequestDto {

    private int page;
    private int size;
    @NotNull(message = "검색할 내용을 입력해주세요.")
    @NotBlank(message = "검색할 내용을 입력해주세요.")
    @NotEmpty(message = "검색할 내용을 입력해주세요.")
    private String text;
    private SearchPostType searchPostType;
}
