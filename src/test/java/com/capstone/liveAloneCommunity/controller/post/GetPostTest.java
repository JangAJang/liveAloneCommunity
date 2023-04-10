package com.capstone.liveAloneCommunity.controller.post;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.post.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
public class GetPostTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostService postService;
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("토큰이 있는 상태로 존재하는 게시물을 조회할 때 200코드와 해당 게시물의 정보가 반환된다.")
    public void getPostTest_Success() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/post?id=1")
                .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.id").value(1))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 있는 상태로 존재하는 게시물을 조회할 때 200코드와 해당 게시물의 정보가 반환된다.")
    public void getPostTest_Fail_Unauthorized() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/post?id=1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @BeforeEach
    void initData(){
        IntStream.range(1, 11).forEach(i -> {
            register(i);
            Member member = memberRepository.findByUsername_Username("test"+i)
                    .orElseThrow(MemberNotFoundException::new);
            writePostsWithMember(member);
        });
    }

    @AfterEach
    void clearDB(){
        databaseCleanup.execute();
    }

    private String getAccessTokenAfterLogIn(int i){
        return authService.logIn(LogInRequestDto.builder()
                .username("test"+i).password("test").build()).getAccessToken();
    }

    private void register(int i){
        authService.register(RegisterRequestDto.builder()
                .username("test"+i)
                .nickname("test"+i)
                .email("test"+i+"@test.com")
                .passwordCheck("test")
                .password("test").build());
    }

    private void writePostsWithMember(Member member){
        IntStream.range(1, 11).forEach(i ->
                postService.writePost(member, WritePostRequestDto.builder()
                        .title("title"+i+" by "+ member.getNickname())
                        .content("content")
                        .category(Category.COOKING).build()));
    }
}
