package com.capstone.liveAloneComunity.dto.member;

import com.capstone.liveAloneComunity.domain.member.MemberInfo;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class EditMemberInfoDto {

    private String nickname;
    private String email;
}
