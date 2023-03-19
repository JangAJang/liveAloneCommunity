package com.capstone.liveAloneComunity.service.member;

import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.dto.member.EditMemberInfoDto;
import com.capstone.liveAloneComunity.entity.member.Member;
import com.capstone.liveAloneComunity.exception.member.EmailNotFormatException;
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
public class EditTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("닉네임과 이메일을 수정할 때, 값을 입력하면 해당 값으로 반환된다.")
    public void editTest() throws Exception{
        //given
        EditMemberInfoDto editMemberInfoDto = EditMemberInfoDto.builder()
                .nickname("newNick")
                .email("new@e.com").build();
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        //when
        memberService.editMember(member.getId(), editMemberInfoDto, member);
        //then
        Assertions.assertThat(member.getNickname()).isEqualTo("newNick");
        Assertions.assertThat(member.getEmail()).isEqualTo("new@e.com");
    }

    @Test
    @DisplayName("닉네임과 이메일을 수정할 때, 이메일 형식이 올바르지 않으면 예외처리한다.")
    public void editFail_EmailFormat() throws Exception{
        //given
        EditMemberInfoDto editMemberInfoDto = EditMemberInfoDto.builder()
                .nickname("newNick")
                .email("newe.com").build();
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        //when

        //then
        Assertions.assertThatThrownBy(() -> memberService.editMember(member.getId(), editMemberInfoDto, member))
                .isInstanceOf(EmailNotFormatException.class);
    }

    @Test
    @DisplayName("자신이 아닌 다른 멤버의 게시물을 수정하려 하면, 예외처리한다.")
    public void editFail_NotAllowed() throws Exception{
        //given
        EditMemberInfoDto editMemberInfoDto = EditMemberInfoDto.builder()
                .nickname("newNick")
                .email("new@e.com").build();
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        authService.register(RegisterRequestDto.builder()
                .username("test1")
                .nickname("test1")
                .email("test1@test.com")
                .password("test1")
                .passwordCheck("test1").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        Member member1 = memberRepository.findByUsername_Username("test1").orElseThrow(MemberNotFoundException::new);
        //when

        //then
        Assertions.assertThatThrownBy(() -> memberService.editMember(member1.getId(), editMemberInfoDto, member))
                .isInstanceOf(MemberNotAllowedException.class);
    }
}
