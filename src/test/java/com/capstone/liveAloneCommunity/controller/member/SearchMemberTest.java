package com.capstone.liveAloneCommunity.controller.member;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.member.MemberSearchType;
import com.capstone.liveAloneCommunity.dto.member.SearchMemberDto;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
public class SearchMemberTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void initData(){
        IntStream.range(1, 501).forEach(i->
                authService.register(RegisterRequestDto.builder()
                        .username("test"+i)
                        .nickname("test"+i)
                        .email("test"+i+"@test.com")
                        .password("test")
                        .passwordCheck("test").build())
        );
    }

    @AfterEach
    void cleanDB(){
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("아이디 기준으로 회원 검색을 성공할 때, 200코드와 해당 회원들이 한 페이지당 size만큼 반환된다.")
    public void searchMemberSuccess_USERNAME() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .memberSearchType(MemberSearchType.USERNAME)
                .text("00")
                .page(0)
                .size(10).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member/search")
                .header("Authorization", getAccessTokenAfterLogIn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(searchMemberDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.searchResult.totalElements").value(5))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("닉네임 기준으로 회원 검색을 성공할 때, 200코드와 해당 회원들이 한 페이지당 size만큼 반환된다.")
    public void searchMemberSuccess_NICKNAME() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .memberSearchType(MemberSearchType.NICKNAME)
                .text("00")
                .page(0)
                .size(10).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member/search")
                .header("Authorization", getAccessTokenAfterLogIn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(searchMemberDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.searchResult.totalElements").value(5))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("이메일 기준으로 회원 검색을 성공할 때, 200코드와 해당 회원들이 한 페이지당 size만큼 반환된다.")
    public void searchMemberSuccess_EMAIL() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .memberSearchType(MemberSearchType.EMAIL)
                .text("00")
                .page(0)
                .size(10).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member/search")
                .header("Authorization", getAccessTokenAfterLogIn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(searchMemberDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.searchResult.totalElements").value(5))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 검색을 성공할 때, access token이 없으면 401에러 코드와 다시 로그인해야 함을 반환한다.")
    public void searchMemberFail_No_Authentication() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .memberSearchType(MemberSearchType.USERNAME)
                .text("00")
                .page(0)
                .size(10).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(searchMemberDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 검색을 할 때, 아무 값도 입력하지 않으면 400에러와 검색 문구를 입력해야함을 반환한다.")
    public void searchMemberFail_Null_Input() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .memberSearchType(MemberSearchType.USERNAME)
                .page(0)
                .size(10).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member/search")
                        .header("Authorization", getAccessTokenAfterLogIn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(searchMemberDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("검색 문구를 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 검색을 할 때, 빈 문자열을 입력하면 400에러와 검색 문구를 입력해야함을 반환한다.")
    public void searchMemberFail_Empty_Input() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .memberSearchType(MemberSearchType.USERNAME)
                .text("")
                .page(0)
                .size(10).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member/search")
                        .header("Authorization", getAccessTokenAfterLogIn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(searchMemberDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("검색 문구를 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("회원 검색을 할 때, 공백 문자열을 입력하면 400에러와 검색 문구를 입력해야함을 반환한다.")
    public void searchMemberFail_Blank_Input() throws Exception{
        //given
        SearchMemberDto searchMemberDto = SearchMemberDto.builder()
                .memberSearchType(MemberSearchType.USERNAME)
                .text("  ")
                .page(0)
                .size(10).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/member/search")
                        .header("Authorization", getAccessTokenAfterLogIn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(searchMemberDto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("검색 문구를 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String getAccessTokenAfterLogIn(){
        authService.register(RegisterRequestDto.builder()
                .username("user")
                .nickname("nickname")
                .email("email@email.com")
                .password("pass")
                .passwordCheck("pass").build());
        return authService.logIn(LogInRequestDto.builder()
                .username("user").password("pass").build()).getAccessToken();
    }

    private String makeJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
