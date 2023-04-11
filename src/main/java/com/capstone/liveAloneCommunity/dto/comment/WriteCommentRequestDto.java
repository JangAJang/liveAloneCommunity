package com.capstone.liveAloneCommunity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WriteCommentRequestDto {
    @NotNull(message = "게시물의 id를 입력해주세요")
    private Long postId;
    @NotBlank(message = "댓글을 입력해주세요")
    private String content;
}
