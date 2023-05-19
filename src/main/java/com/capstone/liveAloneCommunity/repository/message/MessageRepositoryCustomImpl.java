package com.capstone.liveAloneCommunity.repository.message;

import com.capstone.liveAloneCommunity.dto.message.MessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MessageResponseDto> searchMessage(MessageSearchRequestDto messageSearchRequestDto) {
        return null;
    }

    private BooleanExpression checkCondition(MessageSearchRequestDto messageSearchRequestDto,
                                             SearchMessageType searchMessageType, ReadMessageType readMessageType) {
        if (searchMessageType.equals(SearchMessageType.NAME)) {
            return ReadByNameCondition(messageSearchRequestDto, readMessageType);
        }
        if (searchMessageType.equals(SearchMessageType.CONTENT)) {
            return ReadByContentCondition(messageSearchRequestDto)
                    .and(checkReadCondition(readMessageType, messageSearchRequestDto));
        }
        if (searchMessageType.equals(SearchMessageType.NOT)) {
            return checkReadCondition(readMessageType, messageSearchRequestDto);
        }
        return ReadByCalenderCondition(messageSearchRequestDto, searchMessageType, readMessageType);
    }
}
