package com.capstone.liveAloneCommunity.repository.message;

import com.capstone.liveAloneCommunity.dto.message.MessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import com.capstone.liveAloneCommunity.dto.message.QMessageResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static com.capstone.liveAloneCommunity.entity.message.QMessage.message;

@RequiredArgsConstructor
public class MessageRepositoryCustomImpl implements MessageRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MessageResponseDto> searchMessage(MessageSearchRequestDto messageSearchRequestDto) {
        Pageable pageable = PageRequest.of(messageSearchRequestDto.getPage(), messageSearchRequestDto.getSize());
        QueryResults<MessageResponseDto> result = jpaQueryFactory
                .select(new QMessageResponseDto(message.content.content, message.receiver.nickname.nickname,
                        message.sender.nickname.nickname, message.createdDate))
                .from(message)
                .where(checkSearchCondition(messageSearchRequestDto, messageSearchRequestDto.getSearchMessageType()),
                        (checkReadCondition(messageSearchRequestDto, messageSearchRequestDto.getReadMessageType())))
                .orderBy(message.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression checkSearchCondition(MessageSearchRequestDto messageSearchRequestDto,
                                                   SearchMessageType searchMessageType) {
        if (searchMessageType.equals(SearchMessageType.NAME)) {
            return message.sender.nickname.nickname.contains(messageSearchRequestDto.getText())
                    .or(message.receiver.nickname.nickname.contains(messageSearchRequestDto.getText()));
        }
        if (searchMessageType.equals(SearchMessageType.CONTENT)) {
            return message.content.content.contains(messageSearchRequestDto.getText());
        }
        if (searchMessageType.equals(SearchMessageType.NOT)) {
            return null;
        }
        return readByCalenderCondition(messageSearchRequestDto, searchMessageType);
    }

    private BooleanExpression readByCalenderCondition(MessageSearchRequestDto messageSearchRequestDto,
                                                      SearchMessageType searchMessageType) {
        if (searchMessageType.equals(SearchMessageType.YEAR)) {
            return message.createdDate.year().eq(Integer.valueOf(messageSearchRequestDto.getText()));
        }
        if (searchMessageType.equals(SearchMessageType.MONTH)) {
            return message.createdDate.month().eq(Integer.valueOf(messageSearchRequestDto.getText()));
        }
        return message.createdDate.dayOfMonth().eq(Integer.valueOf(messageSearchRequestDto.getText()));
    }

    private BooleanExpression checkReadCondition(MessageSearchRequestDto messageSearchRequestDto,
                                                 ReadMessageType readMessageType) {
        if (readMessageType.equals(ReadMessageType.SENDER)) {
            return readSenderCondition(messageSearchRequestDto, readMessageType);
        }
        if (readMessageType.equals(ReadMessageType.RECEIVER)) {
            return readReceiverCondition(messageSearchRequestDto, readMessageType);
        }
        return readSenderCondition(messageSearchRequestDto, readMessageType)
                .or(readReceiverCondition(messageSearchRequestDto, readMessageType));
    }

    private BooleanExpression readReceiverCondition(MessageSearchRequestDto messageSearchRequestDto, ReadMessageType readMessageType) {
        return message.receiver.nickname.nickname.eq(messageSearchRequestDto.getMember())
                .and(message.deletedByReceiver.not());
    }

    private BooleanExpression readSenderCondition(MessageSearchRequestDto messageSearchRequestDto, ReadMessageType readMessageType) {
        return message.sender.nickname.nickname.eq(messageSearchRequestDto.getMember())
                .and(message.deletedBySender.not());
    }
}
