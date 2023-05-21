package com.capstone.liveAloneCommunity.dto.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
public class ReadCommentResponseDto {

    private String createdDate;
    private String nickname;
    private String content;

    public static ReadCommentResponseDto toDto(Comment comment) {
        return ReadCommentResponseDto.builder()
                .content(comment.getContent())
                .nickname(comment.getWriterName())
                .createdDate(getCreatedDateToString(comment.getCreatedDate()))
                .build();
    }

    private static String getCreatedDateToString(LocalDateTime createdDate){
        return createdDate.getYear() + ". " +createdDate.getMonthValue() + ". " + createdDate.getDayOfMonth()
                + ". " + createdDate.getHour() + ":" + createdDate.getMinute();
    }
}
