package com.capstone.liveAloneCommunity.service.message;

import com.capstone.liveAloneCommunity.dto.message.MessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import com.capstone.liveAloneCommunity.dto.message.MultiMessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.WriteMessageRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.message.Message;
import com.capstone.liveAloneCommunity.exception.message.MessageNotFoundException;
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
        Message message = new Message(member, receiver, writeMessageRequestDto.getContent());
        messageRepository.save(message);
        return MessageResponseDto.toDto(message);
    }

    public MessageResponseDto readMessage(Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        return MessageResponseDto.toDto(message);
    }

    public MultiMessageResponseDto readMessageByReceiver(MessageSearchRequestDto messageSearchRequestDto) {
        return new MultiMessageResponseDto(messageRepository.searchMessage(messageSearchRequestDto).getContent());
    }

    public MultiMessageResponseDto readMessageBySender(MessageSearchRequestDto messageSearchRequestDto) {
        return new MultiMessageResponseDto(messageRepository.searchMessage(messageSearchRequestDto).getContent());
    }

    public MultiMessageResponseDto readMessageAll(MessageSearchRequestDto messageSearchRequestDto) {
        return new MultiMessageResponseDto(messageRepository.searchMessage(messageSearchRequestDto).getContent());
    }

    public MultiMessageResponseDto readMessageBySearch(MessageSearchRequestDto messageSearchRequestDto) {
        return new MultiMessageResponseDto(messageRepository.searchMessage(messageSearchRequestDto).getContent());
    }

    public void deleteMessage(Member member, Long id) {
        Message message = messageRepository.findById(id).orElseThrow(MessageNotFoundException::new);
        if (message.getSender().equals(member)) {
            message.deletedBySender();
        }
        if (message.getReceiver().equals(member)) {
            message.deleteByReceiver();
        }
        if (message.isDeletedMessage()) {
            messageRepository.delete(message);
        }
    }
}
