package com.capstone.liveAloneCommunity.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class SearchMemberDto {

    private int page;
    private int size;
    @NotNull(message = "검색 문구를 입력하세요.")
    @NotEmpty(message = "검색 문구를 입력하세요.")
    @NotBlank(message = "검색 문구를 입력하세요.")
    private String text;
    private MemberSearchType memberSearchType;
}
