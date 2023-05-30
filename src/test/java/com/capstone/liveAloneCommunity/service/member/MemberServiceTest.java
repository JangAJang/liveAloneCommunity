package com.capstone.liveAloneCommunity.service.member;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.member.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
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
    @DisplayName("닉네임을 수정할 때, 값을 입력하면 해당 값으로 반환된다.")
    public void editTest() throws Exception{
        //given
        EditNicknameDto editNicknameDto = EditNicknameDto.builder()
                .nickname("newNick").build();
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        //when
        memberService.editNickname(editNicknameDto, member);
        //then
        Assertions.assertThat(member.getNickname()).isEqualTo("newNick");
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
        memberService.deleteMember(member);
    }
}
