package com.capstone.liveAloneCommunity.service.member;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.member.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.EmailNotFormatException;
import com.capstone.liveAloneCommunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void addTestData(){
        memberRepository.deleteAll();
        IntStream.range(1, 11).forEach(i ->
                authService.register(RegisterRequestDto.builder()
                        .username("test"+i)
                        .nickname("test"+i)
                        .email("test"+i+"@test.com")
                        .password("test")
                        .passwordCheck("test").build())
        );
    }

    @Test
    @DisplayName("아이디에 test를 검색하면 결과가 10개씩 페이징처리되어 반환된다.")
    public void searchTest_Username() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .page(0)
                .size(10)
                .text("test")
                .memberSearchType(MemberSearchType.USERNAME).build();
        //when
        MemberSearchResultDto memberSearchResultDto = memberService.searchMember(searchMemberDto);
        //then
        Assertions.assertThat(memberSearchResultDto.getSearchResult().stream().map(MemberResponseDto::getUsername))
                .containsExactly("test1", "test2", "test3", "test4", "test5",
                        "test6", "test7", "test8", "test9", "test10");
    }

    @Test
    @DisplayName("닉네임 test를 검색하면 결과가 10개씩 페이징처리되어 반환된다.")
    public void searchTest_Nickname() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .text("test")
                .memberSearchType(MemberSearchType.NICKNAME)
                .page(0)
                .size(10).build();
        //when
        MemberSearchResultDto memberSearchResultDto = memberService.searchMember(searchMemberDto);
        //then
        Assertions.assertThat(memberSearchResultDto.getSearchResult().stream().map(MemberResponseDto::getUsername))
                .containsExactly("test1", "test2", "test3", "test4", "test5",
                        "test6", "test7", "test8", "test9", "test10");
    }

    @Test
    @DisplayName("이메일에 1을 검색하면 결과가 1, 10이 페이징처리되어 반환된다.")
    public void searchTest_Email() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .text("1")
                .memberSearchType(MemberSearchType.EMAIL)
                .page(0)
                .size(10).build();
        //when
        MemberSearchResultDto memberSearchResultDto = memberService.searchMember(searchMemberDto);
        //then
        Assertions.assertThat(memberSearchResultDto.getSearchResult().stream().map(MemberResponseDto::getUsername))
                .containsExactly("test1",  "test10");
    }

    @Test
    @DisplayName("닉네임과 이메일을 수정할 때, 값을 입력하면 해당 값으로 반환된다.")
    public void editTest() throws Exception{
        //given
        EditNicknameDto editNicknameDto = EditNicknameDto.builder()
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
        memberService.editNickname(member.getId(), editNicknameDto, member);
        //then
        Assertions.assertThat(member.getNickname()).isEqualTo("newNick");
        Assertions.assertThat(member.getEmail()).isEqualTo("new@e.com");
    }

    @Test
    @DisplayName("닉네임과 이메일을 수정할 때, 이메일 형식이 올바르지 않으면 예외처리한다.")
    public void editFail_EmailFormat() throws Exception{
        //given
        EditNicknameDto editNicknameDto = EditNicknameDto.builder()
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
        Assertions.assertThatThrownBy(() -> memberService.editNickname(member.getId(), editNicknameDto, member))
                .isInstanceOf(EmailNotFormatException.class);
    }

    @Test
    @DisplayName("자신이 아닌 다른 멤버의 게시물을 수정하려 하면, 예외처리한다.")
    public void editFail_NotAllowed() throws Exception{
        //given
        EditNicknameDto editNicknameDto = EditNicknameDto.builder()
                .nickname("newNick")
                .email("new@e.com").build();
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        authService.register(RegisterRequestDto.builder()
                .username("testA")
                .nickname("testA")
                .email("testa@test.com")
                .password("test1")
                .passwordCheck("test1").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        Member member1 = memberRepository.findByUsername_Username("test1").orElseThrow(MemberNotFoundException::new);
        //when

        //then
        Assertions.assertThatThrownBy(() -> memberService.editNickname(member1.getId(), editNicknameDto, member))
                .isInstanceOf(MemberNotAllowedException.class);
    }

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
                .username("testA")
                .nickname("testA")
                .email("testa@test.com")
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
