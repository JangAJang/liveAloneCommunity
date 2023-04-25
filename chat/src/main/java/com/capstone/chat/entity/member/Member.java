package com.capstone.chat.entity.member;

import com.capstone.chat.domain.member.Email;
import com.capstone.chat.domain.member.Nickname;
import com.capstone.chat.domain.member.Password;
import com.capstone.chat.domain.member.Username;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Username username;
    @Embedded
    private Nickname nickname;
    @Embedded
    private Email email;
    @Embedded
    private Password password;
    @Column(name = "MEMBER_ROLE")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public String getNickname(){
        return nickname.getNickname();
    }
}
