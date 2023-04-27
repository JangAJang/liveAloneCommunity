package com.capstone.chat.entity.member;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    private Long id;
    private String username;
    private String nickname;
    private String email;
    private String password;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public String getNickname(){
        return nickname;
    }
}
