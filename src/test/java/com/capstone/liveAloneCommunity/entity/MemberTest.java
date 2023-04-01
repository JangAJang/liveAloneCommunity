package com.capstone.liveAloneCommunity.entity;

import com.capstone.liveAloneCommunity.domain.member.Email;
import com.capstone.liveAloneCommunity.domain.member.Nickname;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class MemberTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원을 생성했을 때, 입력 파라미터를 그대로 받아 인스턴스를 초기화시킨다. 이후 getter를 통해 해당 값을 조회할 수 있다.")
    public void createAndGetTest() throws Exception{
        //given
        Username username = new Username("username");
        Password password = new Password("password");
        Nickname nickname = new Nickname("nickname");
        Email email = new Email("email");
        //when
        Member member = new Member(username, nickname, email, password, Role.USER);
        //then
        Assertions.assertThat(member.getUsername()).isEqualTo(username.getUsername());
        Assertions.assertThat(member.getPassword()).isEqualTo(password.getPassword());
        Assertions.assertThat(member.getNickname()).isEqualTo(nickname.getNickname());
        Assertions.assertThat(member.getEmail()).isEqualTo(email.getEmail());
        Assertions.assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("회원 생성시에 입력한 비밀번호와 같은 비밀번호를 파라미터로 받으면, 참을 반환한다. ")
    public void isRightPasswordTest() throws Exception{
        //given
        Username username = new Username("username");
        Password password = new Password(passwordEncoder.encode("password"));
        Nickname nickname = new Nickname("nickname");
        Email email = new Email("email");
        //when
        Member member = new Member(username, nickname, email, password, Role.USER);
        //then
        Assertions.assertThat(member.isRightPassword("password", passwordEncoder)).isTrue();
    }

    @Test
    @DisplayName("회원 생성시에 입력한 비밀번호와 다른 비밀번호를 파라미터로 받으면, 거짓을 반환한다. ")
    public void isRightPasswordTest_FAIL() throws Exception{
        //given
        Username username = new Username("username");
        Password password = new Password(passwordEncoder.encode("password"));
        Nickname nickname = new Nickname("nickname");
        Email email = new Email("email");
        //when
        Member member = new Member(username, nickname, email, password, Role.USER);
        //then
        Assertions.assertThat(member.isRightPassword("password1", passwordEncoder)).isFalse();
    }
}
