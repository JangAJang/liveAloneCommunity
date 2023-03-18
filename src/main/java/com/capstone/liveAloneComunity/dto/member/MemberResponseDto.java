package com.capstone.liveAloneComunity.dto.member;

import com.capstone.liveAloneComunity.entity.Member;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class MemberResponseDto {

    private long id;
    private String username;
    private String nickname;
    private String email;

    public static MemberResponseDto toDto(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .username(member.getUsername())
                .nickname(member.getNickname())
                .email(member.getEmail()).build();
    }
}
