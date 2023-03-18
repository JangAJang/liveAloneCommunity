package com.capstone.liveAloneComunity.entity;

import com.capstone.liveAloneComunity.domain.member.*;
import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public Member(RegisterRequestDto registerRequestDto, PasswordEncoder passwordEncoder){
        this.username = new Username(registerRequestDto.getUsername());
        this.memberInfo = new MemberInfo(registerRequestDto.getNickname(), registerRequestDto.getEmail());
        this.password = new Password(passwordEncoder.encode(registerRequestDto.getPassword()));
        this.role = Role.USER;
    }

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

    public boolean isRightPassword(String password, PasswordEncoder passwordEncoder){
        return this.password.equals(password, passwordEncoder);
    }

    public String getPassword(){
        return this.password.getPassword();
    }

    public String getNickname(){
        return memberInfo.getNickname();
    }

    public String getEmail(){
        return memberInfo.getEmail();
    }

    public void editInfo(String nickname, String email) {
        this.memberInfo = new MemberInfo(nickname, email);
    }
}
