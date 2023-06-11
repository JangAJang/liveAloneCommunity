package com.capstone.liveAloneCommunity.entity.member;

import com.capstone.liveAloneCommunity.domain.member.*;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;
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
    @Column
    private Point point;

    @SneakyThrows
    public Member(RegisterRequestDto registerRequestDto, PasswordEncoder passwordEncoder){
        this.username = new Username(registerRequestDto.getUsername());
        this.nickname = new Nickname(registerRequestDto.getNickname());
        this.email = new Email(registerRequestDto.getEmail());
        this.password = new Password(passwordEncoder.encode(registerRequestDto.getPassword()));
        this.role = Role.USER;
        this.point = (Point) new WKTReader().read(String.format("POINT(%f %f)", 37.22214637050458, 127.18653080961145));
    }

    @SneakyThrows
    @Builder
    public Member(Username username, Nickname nickname, Email email, Password password, Role role, String provider, String providerId){
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.point = (Point) new WKTReader().read(String.format("POINT(%f %f)", 37.22214637050458, 127.18653080961145));
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
        return nickname.getNickname();
    }

    public String getEmail(){
        return email.getEmail();
    }

    public void editNickname(String nickname) {
        this.nickname = new Nickname(nickname);
    }

    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = new Password(passwordEncoder.encode(newPassword));
    }

    public Point getPoint(){
        return point;
    }

    public void changePoint(Point point) {
        this.point = point;
    }
}
