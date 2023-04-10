package com.capstone.liveAloneCommunity.controller.post;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.post.PostService;
import org.assertj.core.api.Assertions;
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
class DeletePostTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private PostService postService;
    @Autowired
    private DatabaseCleanup cleanup;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

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
        cleanup.execute();
    }

    @Test
    @DisplayName("토큰이 있는 상태로, 자신의 게시물을 삭제요청하면 200코드와 삭제 성공함을 반환한다.")
    public void deletePostTest_Success() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.delete("/api/post/delete?id=1")
                .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print());
        Assertions.assertThat(postRepository.findById(1L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("토큰이 없는 상태로, 자신의 게시물을 삭제요청하면 401코드와 다시 로그인해야함을 알려주며 게시물을 삭제되지 않는다.")
    public void deletePostTest_Fail_Unauthorized() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.delete("/api/post/delete?id=1"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
        Assertions.assertThat(postRepository.findById(1L).isPresent()).isTrue();
    }

    @Test
    @DisplayName("토큰이 있는 상태로, 존재하지 않는 게시물을 삭제요청하면 404에러와 존재하지 않는 게시물임을 알려준다.")
    public void deletePostTest_Fail_Post_Not_Found() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.delete("/api/post/delete?id=10000")
                        .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("해당 게시물을 찾을 수 없습니다."))
                .andDo(MockMvcResultHandlers.print());
        Assertions.assertThat(postRepository.findById(10000L).isPresent()).isFalse();
    }

    @Test
    @DisplayName("토큰이 있는 상태로, 다른 사람의 게시물을 삭제요청하면 401에러와 권한이 없음을 알려준다.")
    public void deletePostTest_Fail_Not_My_Post() throws Exception{
        //given
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.delete("/api/post/delete?id=20")
                        .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("권한이 없습니다."))
                .andDo(MockMvcResultHandlers.print());
        Assertions.assertThat(postRepository.findById(20L).isPresent()).isTrue();
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
