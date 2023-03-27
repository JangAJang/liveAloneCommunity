package com.capstone.liveAloneCommunity.dto.post;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.capstone.liveAloneCommunity.dto.post.QPostResponseDto is a Querydsl Projection type for PostResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostResponseDto extends ConstructorExpression<PostResponseDto> {

    private static final long serialVersionUID = -1931264859L;

    public QPostResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> writer, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<com.capstone.liveAloneCommunity.domain.post.Category> category) {
        super(PostResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class, com.capstone.liveAloneCommunity.domain.post.Category.class}, id, writer, title, content, category);
    }

}

