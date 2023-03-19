package com.capstone.liveAloneComunity.dto.member;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.capstone.liveAloneComunity.dto.member.QMemberResponseDto is a Querydsl Projection type for MemberResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMemberResponseDto extends ConstructorExpression<MemberResponseDto> {

    private static final long serialVersionUID = 659004728L;

    public QMemberResponseDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> username, com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> email) {
        super(MemberResponseDto.class, new Class<?>[]{long.class, String.class, String.class, String.class}, id, username, nickname, email);
    }

}

