package com.capstone.liveAloneCommunity.repository.message;

import com.capstone.liveAloneCommunity.dto.message.MessageResponseDto;
import com.capstone.liveAloneCommunity.dto.message.MessageSearchRequestDto;
import org.springframework.data.domain.Page;

public interface MessageRepositoryCustom {
    Page<MessageResponseDto> searchMessage(MessageSearchRequestDto messageSearchRequestDto);
}
