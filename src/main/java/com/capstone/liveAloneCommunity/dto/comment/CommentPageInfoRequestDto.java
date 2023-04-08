package com.capstone.liveAloneCommunity.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentPageInfoRequestDto {
    @NotNull(message = "페이지를 입력해주세요")
    private Integer page;
    @NotNull(message = "size를 입력해주세요")
    private Integer size;
}
