package com.capstone.liveAloneCommunity.controller.post;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.post.EditPostRequestDto;
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

import java.util.stream.IntStream;

@SpringBootTest
@AutoConfigureMockMvc
public class EditPostTest {

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

    @BeforeEach
    void initData(){
        IntStream.range(1, 11).forEach( i ->{
            authService.register(RegisterRequestDto.builder()
                    .username("test"+i)
                    .nickname("test"+i)
                    .email("test"+i+"@test.com")
                    .password("test")
                    .passwordCheck("test").build());
            Member member = memberRepository.findByUsername_Username("test"+i).orElseThrow(MemberNotFoundException::new);
            IntStream.range(1, 6).forEach(each ->
                    postService.writePost(member, WritePostRequestDto.builder()
                            .category(Category.COOKING)
                            .title(member.getNickname()+each)
                            .content("content").build()));
        });
    }

    @Test
    @DisplayName("토큰이 존재할 때, 내가 작성한 게시물의 수정할 제목과 내용을 입력하면 200코드와 수정한 결과가 반환된다.")
    public void editPostTest_Success() throws Exception{
        //given
        EditPostRequestDto editPostRequestDto =  EditPostRequestDto.builder()
                .title("newTitle")
                .content("newContent")
                .id(3L).build();
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/post/edit")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(editPostRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.writer").value("test1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.title").value("newTitle"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.content").value("newContent"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.categoryName").value("COOKING"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 존재하지 않을 때, 401에러와 다시 로그인해야함을 알려준다.")
    public void editPostTest_Fail_Unauthorized() throws Exception{
        //given
        EditPostRequestDto editPostRequestDto =  EditPostRequestDto.builder()
                .title("newTitle")
                .content("newContent")
                .id(3L).build();
        String accessToken = getAccessTokenAfterLogIn(1);
        //expected
        mvc.perform(MockMvcRequestBuilders.patch("/api/post/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(editPostRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String makeJson(Object object)throws Exception{
        return new ObjectMapper().writeValueAsString(object);
    }

    private String getAccessTokenAfterLogIn(int index){
        return authService.logIn(LogInRequestDto.builder()
                .username("test"+index)
                .password("test").build()).getAccessToken();
    }

    @AfterEach
    void clearDB(){
        databaseCleanup.execute();
    }
}
