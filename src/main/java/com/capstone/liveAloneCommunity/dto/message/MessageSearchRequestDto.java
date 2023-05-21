package com.capstone.liveAloneCommunity.dto.message;

import com.capstone.liveAloneCommunity.repository.message.ReadMessageType;
import com.capstone.liveAloneCommunity.repository.message.SearchMessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageSearchRequestDto {
    @NotBlank(message = "회원 정보를 입력해주세요")
    private String requestMember;
    private String text;
    private int page;
    private int size;
    private SearchMessageType searchMessageType;
    private ReadMessageType readMessageType;
}
