package com.capstone.liveAloneCommunity.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SearchMemberDto {

    private int page;

    private int size;

    @NotBlank(message = "검색 문구를 입력하세요.")
    private String text;

    private MemberSearchType memberSearchType;
}
