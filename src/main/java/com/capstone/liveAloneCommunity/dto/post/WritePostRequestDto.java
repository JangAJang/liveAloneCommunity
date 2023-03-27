package com.capstone.liveAloneCommunity.dto.post;

import com.capstone.liveAloneCommunity.domain.post.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WritePostRequestDto {

    private Category category;

    @NotNull(message = "제목을 입력하세요.")
    @NotEmpty(message = "제목을 입력하세요.")
    @NotBlank(message = "제목을 입력하세요.")
    private String title;
    @NotNull(message = "내용을 입력하세요.")
    @NotEmpty(message = "내용을 입력하세요.")
    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    @Builder
    public WritePostRequestDto(Category category, String title, String content){
        this.category = category;
        this.title = title;
        this.content = content;
    }
}
