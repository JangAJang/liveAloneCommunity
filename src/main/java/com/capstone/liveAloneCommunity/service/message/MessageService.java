package com.capstone.liveAloneCommunity.service.message;

import com.capstone.liveAloneCommunity.dto.message.MessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import com.capstone.liveAloneCommunity.dto.message.MultiMessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.WriteMessageRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.message.Message;
import com.capstone.liveAloneCommunity.exception.message.MessageNotFoundException;
import com.capstone.liveAloneCommunity.exception.message.CanNotSameReceiverAndSenderException;
import com.capstone.liveAloneCommunity.repository.message.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageResponseDto writeMessage(Member member, Member receiver, WriteMessageRequestDto writeMessageRequestDto) {
        isSameSenderAndReceiver(member, receiver);
        Message message = new Message(member, receiver, writeMessageRequestDto.getContent());
        messageRepository.save(message);
        return MessageResponseDto.toDto(message);
    }

    public MessageResponseDto readMessage(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
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
}
