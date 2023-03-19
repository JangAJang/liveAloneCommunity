package com.capstone.liveAloneComunity.service.member;

import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.entity.member.Member;
import com.capstone.liveAloneComunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import com.capstone.liveAloneComunity.service.auth.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class DeleteTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("삭제 요청시에 회원이 존재하면 삭제시킨다. ")
    public void deleteSuccess() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test")
                .build());
        //when
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        //then
        memberService.deleteMember(member.getId(), member);
    }

    @Test
    @DisplayName("삭제 실패시에 해당 회원이 존재하지 않는 것이기 때문에, MemberNotFoundException을 반환한다. ")
    public void deleteFail() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test")
                .build());
        //when
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        memberRepository.deleteAll();
        //then
        Assertions.assertThatThrownBy(()->memberService.deleteMember(member.getId(), member))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("다른 회원을 삭제하려할 때, 예외를 반환한다.")
    public void deleteFail_NotAuthorized() throws Exception{
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test")
                .build());
        authService.register(RegisterRequestDto.builder()
                .username("test1")
                .nickname("test1")
                .email("test1@test.com")
                .password("test1")
                .passwordCheck("test1")
                .build());
        //when
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        Member member1 = memberRepository.findByUsername_Username("test1").orElseThrow(MemberNotFoundException::new);
        //then
        Assertions.assertThatThrownBy(()->memberService.deleteMember(member.getId(), member1))
                .isInstanceOf(MemberNotAllowedException.class);
    }
}
