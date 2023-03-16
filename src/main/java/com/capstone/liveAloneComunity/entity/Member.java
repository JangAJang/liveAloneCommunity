package com.capstone.liveAloneComunity.entity;

import com.capstone.liveAloneComunity.domain.member.*;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Username username;

    @Embedded
    private MemberInfo memberInfo;

    @Embedded
    private Password password;

    @Column(name = "MEMBER_ROLE")
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Builder
    public Member(Username username, MemberInfo memberInfo, Password password, Role role) {
        this.username = username;
        this.memberInfo = memberInfo;
        this.password = password;
        this.role = role;
    }

    public String getUsername(){
        return this.username.getUsername();
    }

    public boolean isRightPassword(String password){
        return this.password.equals(password);
    }

    public String getPassword(){
        return this.password.getPassword();
    }
}
