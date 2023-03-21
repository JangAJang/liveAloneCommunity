package com.capstone.liveAloneComunity.repository.member;

import com.capstone.liveAloneComunity.dto.member.MemberResponseDto;
import com.capstone.liveAloneComunity.dto.member.MemberSearchType;
import com.capstone.liveAloneComunity.dto.member.QMemberResponseDto;
import com.capstone.liveAloneComunity.dto.member.SearchMemberDto;
import com.capstone.liveAloneComunity.entity.member.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.capstone.liveAloneComunity.entity.member.QMember.*;


@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory query;

    @Override
    public Page<MemberResponseDto> searchMember(SearchMemberDto searchMemberDto, Pageable pageable) {
        QueryResults<MemberResponseDto> result = query.select(new QMemberResponseDto(member.id,
                        member.username.username,
                        member.memberInfo.nickname, member.memberInfo.email))
                .from(member)
                .where(getContains(searchMemberDto))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<MemberResponseDto>(result.getResults(), pageable, result.getTotal());
    }

    private static BooleanExpression getContains(SearchMemberDto searchMemberDto) {
        MemberSearchType searchType = searchMemberDto.getMemberSearchType();
        if(searchType.equals(MemberSearchType.USERNAME))
            return member.username.username.contains(searchMemberDto.getText());
        if(searchType.equals(MemberSearchType.NICKNAME))
            return member.memberInfo.nickname.contains(searchMemberDto.getText());
        return member.memberInfo.email.contains(searchMemberDto.getText());
    }
}
