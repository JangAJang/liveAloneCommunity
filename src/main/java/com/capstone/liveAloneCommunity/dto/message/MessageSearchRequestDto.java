package com.capstone.liveAloneCommunity.dto.message;

import com.capstone.liveAloneCommunity.repository.message.ReadMessageType;
import com.capstone.liveAloneCommunity.repository.message.SearchMessageType;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSearchRequestDto {

    private String member;
    private String text;
    private int page;
    private int size;
    private SearchMessageType searchMessageType;
    private ReadMessageType readMessageType;
}
