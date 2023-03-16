package com.capstone.liveAloneComunity.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberDomainTest {

    @Test
    @DisplayName("Username을 생성할 때, 문자열을 파라미터로 받으며 getter로 반환시에 저장했던 문자열이 반환된다.")
    public void usernameCreateTest() throws Exception{
        //given
        String id = "아이디";
        //when
        Username username = new Username(id);
        //then
        Assertions.assertThat(username.getUsername()).isEqualTo(id);
    }

    @Test
    @DisplayName("MemberInfo 도메인을 생성하면, 닉네임과 이메일을 가지는 변수를 만든다.")
    public void createTest() throws Exception{
        //given
        String nickname = "닉네임";
        String email = "이메일";
        //when
        MemberInfo memberInfo = new MemberInfo(nickname, email);
        //then
        Assertions.assertThat(memberInfo.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(memberInfo.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("비밀번호를 생성할 때 파라미터로 인스턴스를 초기화시키고, getPassword메서드로 초기화시켰던 값을 반환한다.")
    public void passwordCreateTest() throws Exception{
        //given
        String pw = "비밀번호";
        //when
        Password password = new Password(pw);
        //then
        Assertions.assertThat(password.getPassword()).isEqualTo(pw);
    }

    @Test
    @DisplayName("Password의 equals는 두 문자열이 같으면 참, 다르면 거짓을 반환한다. ")
    public void passwordEqualsTest() throws Exception{
        //given
        String pw = "비밀번호";
        //when
        Password password = new Password(pw);
        //then
        Assertions.assertThat(password.equals(pw)).isTrue();
        Assertions.assertThat(password.equals(pw + "1")).isFalse();
    }
}
