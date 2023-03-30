package com.capstone.liveAloneCommunity.dto.post;

import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostResponseDto {
    private Long id;
    private String writer;
    private String title;
    private String content;
    private String categoryName;
    private String createdDate;

    @Builder
    @QueryProjection
    public PostResponseDto(Long id, String writer, String title, String content, Category category, LocalDateTime createdDate) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.categoryName = category.getName();
        this.createdDate = getCreatedDateToString(createdDate);
    }

    private String getCreatedDateToString(LocalDateTime createdDate){
        return createdDate.getYear() + ". " +createdDate.getMonthValue() + ". " + createdDate.getDayOfMonth()
                + ". " + createdDate.getHour() + ":" + createdDate.getDayOfMonth();
    }

    public static PostResponseDto toDto(Post post){
        return PostResponseDto.builder()
                .id(post.getId())
                .category(post.getCategory())
                .writer(post.getWritersName())
                .title(post.getTitle())
                .content(post.getContent())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
