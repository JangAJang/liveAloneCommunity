package com.capstone.liveAloneCommunity.dto.member;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class EditMemberInfoDto {

    private String nickname;
    private String email;
}
