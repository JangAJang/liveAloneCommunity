package com.capstone.liveAloneCommunity.controller.post;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.post.MembersPostRequestDto;
import com.capstone.liveAloneCommunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.post.PostService;
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

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
class GetMembersPostTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void initData(){
        IntStream.range(1, 6).forEach(i -> {
            Member member = createMember(i);
            try {
                createPost(member);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    @AfterEach
    void clearDB(){
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("토큰이 있는 상태로 회원의 게시물을 조회하면 200코드와 최근 게시물순으로 해당 회원의 게시물을 조회한다.")
    public void getMembersPost_Success() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn();
        MembersPostRequestDto membersPostRequestDto = MembersPostRequestDto.builder()
                .page(0)
                .size(10)
                .id(1L).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/post/of")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(membersPostRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.result[0].title").value("SINGLE_NEWS By test1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.result[1].title").value("VILLAGE_INFO By test1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.result[2].title").value("LOST By test1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.result[3].title").value("HOBBY_SHARE By test1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.result[4].title").value("COOKING By test1"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 없는 상태로 회원의 게시물을 조회하면 401코드와 다시 로그인해야함을 알린다.")
    public void getMembersPost_Fail_Unauthorized() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn();
        MembersPostRequestDto membersPostRequestDto = MembersPostRequestDto.builder()
                .page(0)
                .size(10)
                .id(1L).build();
        //expected
        mvc.perform(MockMvcRequestBuilders.get("/api/post/of")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(membersPostRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String makeJson(Object object)throws Exception{
        return new ObjectMapper().writeValueAsString(object);
    }

    private String getAccessTokenAfterLogIn(){
        return authService.logIn(LogInRequestDto
                .builder()
                .username("test1")
                .password("test").build()).getAccessToken();
    }

    private Member createMember(int i){
        authService.register(RegisterRequestDto.builder()
                .username("test"+i)
                .nickname("test"+i)
                .email("test"+i+"@test.com")
                .password("test")
                .passwordCheck("test").build());
        return memberRepository.findByUsername_Username("test"+i)
                .orElseThrow(MemberNotFoundException::new);
    }

    private void createPost(Member member)throws Exception{
        postService.writePost(member, WritePostRequestDto.builder()
                .title("COOKING By " + member.getNickname())
                .content("COOKING By " + member.getNickname())
                .category(Category.COOKING).build());
        TimeUnit.SECONDS.sleep(1);
        postService.writePost(member, WritePostRequestDto.builder()
                .title("HOBBY_SHARE By " + member.getNickname())
                .content("HOBBY_SHARE By " + member.getNickname())
                .category(Category.HOBBY_SHARE).build());
        TimeUnit.SECONDS.sleep(1);
        postService.writePost(member, WritePostRequestDto.builder()
                .title("LOST By " + member.getNickname())
                .content("LOST By " + member.getNickname())
                .category(Category.LOST).build());
        TimeUnit.SECONDS.sleep(1);
        postService.writePost(member, WritePostRequestDto.builder()
                .title("VILLAGE_INFO By " + member.getNickname())
                .content("VILLAGE_INFO By " + member.getNickname())
                .category(Category.VILLAGE_INFO).build());
        TimeUnit.SECONDS.sleep(1);
        postService.writePost(member, WritePostRequestDto.builder()
                .title("SINGLE_NEWS By " + member.getNickname())
                .content("SINGLE_NEWS By " + member.getNickname())
                .category(Category.SINGLE_NEWS).build());
        TimeUnit.SECONDS.sleep(1);
    }
}
