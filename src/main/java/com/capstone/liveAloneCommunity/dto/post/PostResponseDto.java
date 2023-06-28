package com.capstone.liveAloneCommunity.dto.post;

import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private String categoryName;
    private String createdDate;

    @Builder
    @QueryProjection
    public PostResponseDto(final Long id,
                           final String writer,
                           final String title,
                           final String content,
                           final Category category,
                           final LocalDateTime createdDate) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.categoryName = category.getName();
        this.createdDate = getCreatedDateToString(createdDate);
    }

    private String getCreatedDateToString(final LocalDateTime createdDate) {
        return createdDate.getYear() + ". " +createdDate.getMonthValue() + ". " + createdDate.getDayOfMonth()
                + ". " + createdDate.getHour() + ":" + createdDate.getMinute();
    }

    private static String parseCreatedDate(final LocalDateTime createdDate) {
        return createdDate.getYear() + ". " +createdDate.getMonthValue() + ". " + createdDate.getDayOfMonth()
                + ". " + createdDate.getHour() + ":" + createdDate.getMinute();
    }

    public static PostResponseDto from(final Post post) {
        return new PostResponseDto(post.getId(), post.getWritersName(),
                post.getTitle(), post.getContent(),
                post.getCategory().getDescription(),
                parseCreatedDate(post.getCreatedDate()));
    }

    public static PostResponseDto from(final Post post, final Member member) {
        return new PostResponseDto(post.getId(), member.getNickname(),
                post.getTitle(), post.getContent(),
                post.getCategory().getDescription(),
                parseCreatedDate(post.getCreatedDate()));
    }
}
