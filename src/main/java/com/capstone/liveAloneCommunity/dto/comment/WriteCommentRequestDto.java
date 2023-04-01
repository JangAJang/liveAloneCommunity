package com.capstone.liveAloneCommunity.dto.comment;

import lombok.Getter;

@Getter
public class WriteCommentRequestDto {

    private Long postId;
    private String content;
}
