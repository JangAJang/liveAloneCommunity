package com.capstone.liveAloneCommunity.dto.post;

import com.capstone.liveAloneCommunity.domain.post.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class WritePostRequestDto {

    @NotNull(message = "카테고리를 선택해주세요.")
    private Category category;
    @NotBlank(message = "제목을 입력하세요.")
    private String title;
    @NotBlank(message = "내용을 입력하세요.")
    private String content;
}
