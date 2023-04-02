package com.capstone.liveAloneCommunity.dto.member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor(staticName = "toDto")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberSearchResultDto {

    private Page<MemberResponseDto> searchResult;
}
