package com.capstone.liveAloneCommunity.controller.comment;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static com.capstone.liveAloneCommunity.domain.post.Category.*;

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
}
