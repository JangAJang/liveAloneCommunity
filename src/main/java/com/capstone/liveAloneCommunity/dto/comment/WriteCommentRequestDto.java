package com.capstone.liveAloneCommunity.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class WriteCommentRequestDto {

    private long postId;
    private String content;
}
