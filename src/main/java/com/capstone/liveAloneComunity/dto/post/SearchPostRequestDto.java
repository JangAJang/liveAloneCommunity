package com.capstone.liveAloneComunity.dto.post;

import com.capstone.liveAloneComunity.repository.post.SearchPostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SearchPostRequestDto {

    @NotNull(message = "검색할 내용을 입력해주세요.")
    @NotBlank(message = "검색할 내용을 입력해주세요.")
    @NotEmpty(message = "검색할 내용을 입력해주세요.")
    private String text;

    private SearchPostType searchPostType;

    public SearchPostRequestDto(String text, SearchPostType searchPostType) {
        this.text = text;
        this.searchPostType  = searchPostType;
    }
}
