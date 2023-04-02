package com.capstone.liveAloneCommunity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WriteCommentRequestDto {
    private long postId;

    @NotEmpty(message = "댓글을 입력해주세요")
    @NotBlank(message = "댓글을 입력해주세요")
    @NotNull(message = "댓글을 입력해주세요")
    private String content;
}
