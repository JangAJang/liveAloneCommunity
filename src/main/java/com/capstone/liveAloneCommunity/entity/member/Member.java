package com.capstone.liveAloneCommunity.entity.member;

import com.capstone.liveAloneCommunity.domain.member.*;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
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
    private Nickname nickname;
    @Embedded
    private Email email;
    @Embedded
    private Password password;
    @Column(name = "MEMBER_ROLE")
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Column(nullable = true)
    private String providerId;
    @Column
    private String provider;

    public Member(final RegisterRequestDto registerRequestDto, final PasswordEncoder passwordEncoder){
        this.username = new Username(registerRequestDto.getUsername());
        this.nickname = new Nickname(registerRequestDto.getNickname());
        this.email = new Email(registerRequestDto.getEmail());
        this.password = new Password(passwordEncoder.encode(registerRequestDto.getPassword()));
        this.role = Role.USER;
    }

    @Builder
    public Member(final Username username,
                  final Nickname nickname,
                  final Email email,
                  final Password password,
                  final Role role,
                  final String provider,
                  final String providerId)
    {
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }

    public String getUsername() {
        return this.username.getUsername();
    }

    public boolean isRightPassword(final String password, final PasswordEncoder passwordEncoder){
        return this.password.equals(password, passwordEncoder);
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public String getNickname() {
        return nickname.getNickname();
    }

    public String getEmail() {
        return email.getEmail();
    }

    public void editNickname(final String nickname) {
        this.nickname = new Nickname(nickname);
    }

    public void changePassword(final String newPassword, final PasswordEncoder passwordEncoder) {
        this.password = new Password(passwordEncoder.encode(newPassword));
    }
}
