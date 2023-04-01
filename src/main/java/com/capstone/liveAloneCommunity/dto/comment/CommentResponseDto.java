package com.capstone.liveAloneCommunity.dto.comment;

import com.capstone.liveAloneCommunity.entity.comment.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

public class CommentResponseDto {

    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createTime;

    @Builder
    public CommentResponseDto(Long id, String content, String nickname, LocalDateTime createTime) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.createTime = createTime;
    }
}
