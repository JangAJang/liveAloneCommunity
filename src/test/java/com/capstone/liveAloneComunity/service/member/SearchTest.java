package com.capstone.liveAloneComunity.service.member;

import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.dto.member.MemberResponseDto;
import com.capstone.liveAloneComunity.dto.member.MemberSearchResultDto;
import com.capstone.liveAloneComunity.dto.member.MemberSearchType;
import com.capstone.liveAloneComunity.dto.member.SearchMemberDto;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import com.capstone.liveAloneComunity.service.auth.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

@SpringBootTest
@Transactional
public class SearchTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    private static final PageRequest PAGE = PageRequest.of(0, 10);

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
        SearchMemberDto searchMemberDto = new SearchMemberDto("test", MemberSearchType.USERNAME);
        //when
        MemberSearchResultDto memberSearchResultDto = memberService.searchMember(searchMemberDto, PAGE);
        //then
        Assertions.assertThat(memberSearchResultDto.getSearchResult().stream().map(MemberResponseDto::getUsername))
                .containsExactly("test1", "test2", "test3", "test4", "test5",
                        "test6", "test7", "test8", "test9", "test10");
    }

    @Test
    @DisplayName("닉네임 test를 검색하면 결과가 10개씩 페이징처리되어 반환된다.")
    public void searchTest_Nickname() throws Exception{
        //given
        SearchMemberDto searchMemberDto = new SearchMemberDto("test", MemberSearchType.NICKNAME);
        //when
        MemberSearchResultDto memberSearchResultDto = memberService.searchMember(searchMemberDto, PAGE);
        //then
        Assertions.assertThat(memberSearchResultDto.getSearchResult().stream().map(MemberResponseDto::getUsername))
                .containsExactly("test1", "test2", "test3", "test4", "test5",
                        "test6", "test7", "test8", "test9", "test10");
    }

    @Test
    @DisplayName("이메일에 1을 검색하면 결과가 1, 10이 페이징처리되어 반환된다.")
    public void searchTest_Email() throws Exception{
        //given
        SearchMemberDto searchMemberDto = new SearchMemberDto("1", MemberSearchType.EMAIL);
        //when
        MemberSearchResultDto memberSearchResultDto = memberService.searchMember(searchMemberDto, PAGE);
        //then
        Assertions.assertThat(memberSearchResultDto.getSearchResult().stream().map(MemberResponseDto::getUsername))
                .containsExactly("test1",  "test10");
    }
}
