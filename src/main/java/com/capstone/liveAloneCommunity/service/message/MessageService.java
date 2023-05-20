package com.capstone.liveAloneCommunity.service.message;

import com.capstone.liveAloneCommunity.dto.message.MessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import com.capstone.liveAloneCommunity.dto.message.MultiMessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.WriteMessageRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.message.Message;
import com.capstone.liveAloneCommunity.exception.message.MessageNotFoundException;
import com.capstone.liveAloneCommunity.exception.message.CanNotSameReceiverAndSenderException;
import com.capstone.liveAloneCommunity.exception.message.NotMyMessageException;
import com.capstone.liveAloneCommunity.exception.message.SenderAndMemberNotEqualsException;
import com.capstone.liveAloneCommunity.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageResponseDto writeMessage(Member member, Member receiver, String content) {
        isSameSenderAndReceiver(member, receiver);
        Message message = new Message(member, receiver, content);
        messageRepository.save(message);
        return MessageResponseDto.toDto(message);
    }

    public MessageResponseDto readMessage(Member member, Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        checkMessageMember(message, member);
        return MessageResponseDto.toDto(message);
    }

    public MultiMessageResponseDto readMessageByCondition(MessageSearchRequestDto messageSearchRequestDto) {
        return new MultiMessageResponseDto(messageRepository.searchMessage(messageSearchRequestDto).getContent());
    }

    private void isSameSenderAndReceiver(Member member, Member receiver) {
        if (member.equals(receiver)) {
            throw new CanNotSameReceiverAndSenderException();
        }
    }

    private void checkMessageMember(Message message, Member member) {
        if (!(message.checkMessageReceiver(message, member)) && !(message.checkMessageSender(message, member))) {
            throw new NotMyMessageException();
        }
    }
}
