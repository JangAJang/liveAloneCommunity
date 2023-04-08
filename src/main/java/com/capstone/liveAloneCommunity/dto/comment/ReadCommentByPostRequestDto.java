package com.capstone.liveAloneCommunity.dto.comment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadCommentByPostRequestDto {
    @NotNull(message = "게시물의 id를 입력해주세요.")
    private Long postId;
}
