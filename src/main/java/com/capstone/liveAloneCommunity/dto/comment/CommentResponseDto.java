package com.capstone.liveAloneCommunity.dto.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class CommentResponseDto {

    private  Long id;
    private String content;
    private String nickname;
    private LocalDateTime createTime;

    public static CommentResponseDto toDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getWriterName())
                .createTime(comment.getCreatedDate())
                .build();
    }
}