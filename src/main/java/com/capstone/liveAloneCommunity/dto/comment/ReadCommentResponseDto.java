package com.capstone.liveAloneCommunity.dto.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;
import lombok.*;
@Builder
@Getter
public class ReadCommentResponseDto {

    private String title;
    private String content;

    public static ReadCommentResponseDto toDto(Comment comment) {
        return ReadCommentResponseDto.builder()
                .title(comment.getTitle())
                .content(comment.getContent())
                .build();
    }
}
