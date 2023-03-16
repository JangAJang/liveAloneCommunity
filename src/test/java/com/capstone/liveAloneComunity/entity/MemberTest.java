package com.capstone.liveAloneComunity.entity;

import com.capstone.liveAloneComunity.domain.member.MemberInfo;
import com.capstone.liveAloneComunity.domain.member.Password;
import com.capstone.liveAloneComunity.domain.member.Username;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @Test
    @DisplayName("회원을 생성했을 때, 입력 파라미터를 그대로 받아 인스턴스를 초기화시킨다. 이후 getter를 통해 해당 값을 조회할 수 있다.")
    public void createAndGetTest() throws Exception{
        //given
        Username username = new Username("username");
        Password password = new Password("password");
        MemberInfo memberInfo = new MemberInfo("nickname", "email");
        //when
        Member member = new Member(username, memberInfo, password, Role.USER);
        //then
        Assertions.assertThat(member.getUsername()).isEqualTo(username.getUsername());
        Assertions.assertThat(member.getPassword()).isEqualTo(password.getPassword());
        Assertions.assertThat(member.getMemberInfo()).isEqualTo(memberInfo);
        Assertions.assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("회원 생성시에 입력한 비밀번호와 같은 비밀번호를 파라미터로 받으면, 참을 반환한다. ")
    public void isRightPasswordTest() throws Exception{
        //given
        Username username = new Username("username");
        Password password = new Password("password");
        MemberInfo memberInfo = new MemberInfo("nickname", "email");
        //when
        Member member = new Member(username, memberInfo, password, Role.USER);
        //then
        Assertions.assertThat(member.isRightPassword(password.getPassword())).isTrue();
    }

    @Test
    @DisplayName("회원 생성시에 입력한 비밀번호와 다른 비밀번호를 파라미터로 받으면, 거짓을 반환한다. ")
    public void isRightPasswordTest_FAIL() throws Exception{
        //given
        Username username = new Username("username");
        Password password = new Password("password");
        MemberInfo memberInfo = new MemberInfo("nickname", "email");
        //when
        Member member = new Member(username, memberInfo, password, Role.USER);
        //then
        Assertions.assertThat(member.isRightPassword(password.getPassword()+"1")).isFalse();
    }
}
