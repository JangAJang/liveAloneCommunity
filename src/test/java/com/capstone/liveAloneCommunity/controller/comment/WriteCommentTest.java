package com.capstone.liveAloneCommunity.controller.comment;

import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.comment.WriteCommentRequestDto;
import com.capstone.liveAloneCommunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.capstone.liveAloneCommunity.domain.post.Category.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WriteCommentTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostService postService;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void initDB(){
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("former")
                .nickname("former")
                .email("former@email.com")
                .password("former")
                .passwordCheck("former")
                .build();
        authService.register(registerRequestDto);
        Member member = memberRepository.findByUsername_Username("former")
                .orElseThrow(MemberNotAllowedException::new);
        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .category(COOKING)
                .title("title 1")
                .content("content1")
                .build();
        postService.writePost(member, writePostRequestDto);
    }

    @Test
    @DisplayName("댓글을 작성했을 때 작성하고자 하는 member의 정보가 존재하면 상태코드 200을 출력한다.")
    public void writeTest() throws Exception {
        //given
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("former")
                .password("former")
                .build();
        String token = authService.logIn(logInRequestDto).getAccessToken();
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto(1l, "test");

        //when, then
        mockMvc.perform(post("/api/comment").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(writeCommentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.content").value("test"))
                .andExpect(jsonPath("$.result.data.nickname").value("former"))
                .andDo(print());
    }
}
