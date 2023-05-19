package com.capstone.liveAloneCommunity.repository.message;

import com.capstone.liveAloneCommunity.dto.message.MessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import static com.capstone.liveAloneCommunity.entity.message.QMessage.message;

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

    private BooleanExpression ReadByNameCondition(MessageSearchRequestDto messageSearchRequestDto,
                                                  ReadMessageType readMessageType) {
        if (readMessageType.equals(ReadMessageType.SENDER)) {
            return readByNameAndSenderCondition(messageSearchRequestDto);
        }
        if (readMessageType.equals(ReadMessageType.RECEIVER)) {
            return readByNameAndReceiverCondition(messageSearchRequestDto);
        }
        return readByNameAndReceiverCondition(messageSearchRequestDto)
                .or(readByNameAndSenderCondition(messageSearchRequestDto));
    }

    private static BooleanExpression readByNameAndSenderCondition(MessageSearchRequestDto messageSearchRequestDto) {
        return message.sender.nickname.nickname.contains(messageSearchRequestDto.getText())
                .and(message.receiver.nickname.nickname.eq(messageSearchRequestDto.getMember()));
    }

    private static BooleanExpression readByNameAndReceiverCondition(MessageSearchRequestDto messageSearchRequestDto) {
        return message.receiver.nickname.nickname.contains(messageSearchRequestDto.getText())
                .and(message.sender.nickname.nickname.eq(messageSearchRequestDto.getMember()));
    }

    private static BooleanExpression ReadByContentCondition(MessageSearchRequestDto messageSearchRequestDto) {
        return message.content.content.contains(messageSearchRequestDto.getText());
    }

    private BooleanExpression ReadByCalenderCondition(MessageSearchRequestDto messageSearchRequestDto,
                                                      SearchMessageType searchMessageType, ReadMessageType readMessageType) {
        if (searchMessageType.equals(SearchMessageType.YEAR)) {
            return message.createdDate.year().eq(Integer.valueOf(messageSearchRequestDto.getText()))
                    .and(checkReadCondition(readMessageType, messageSearchRequestDto));
        }
        if (searchMessageType.equals(SearchMessageType.MONTH)) {
            return message.createdDate.month().eq(Integer.valueOf(messageSearchRequestDto.getText()))
                    .and(checkReadCondition(readMessageType, messageSearchRequestDto));
        }
        return message.createdDate.dayOfMonth().eq(Integer.valueOf(messageSearchRequestDto.getText()))
                .and(checkReadCondition(readMessageType, messageSearchRequestDto));
    }
}
