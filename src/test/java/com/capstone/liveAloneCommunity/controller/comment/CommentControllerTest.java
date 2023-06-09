package com.capstone.liveAloneCommunity.controller.comment;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.comment.*;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
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
import static com.capstone.liveAloneCommunity.domain.post.Category.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void initDB(){
        RegisterRequestDto registerRequestDto = RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@email.com")
                .password("test")
                .passwordCheck("test")
                .build();
        authService.register(registerRequestDto);
        Member member = memberRepository.findByUsername_Username("test")
                .orElseThrow(MemberNotAllowedException::new);
        Post post = Post.builder()
                .title(new Title("title"))
                .content(new Content("content"))
                .category(COOKING)
                .member(member)
                .build();
        postRepository.save(post);
        Comment comment = new Comment("testComment", post, member);
        commentRepository.save(comment);
    }

    @AfterEach
    void afterTestCleanData() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("댓글을 작성했을 때 작성하고자 하는 member의 정보가 존재하면 상태코드 200을 출력한다.")
    public void writeTest() throws Exception {
        //given
        String accessToken = logIn();
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto(1L, "test");

        //when, then
        mockMvc.perform(post("/api/comment").header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(writeCommentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.content").value("test"))
                .andExpect(jsonPath("$.result.data.nickname").value("test"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글을 작성할 때 member의 정보가 존재하지 않으면 예외가 터진다.")
    public void memberNotFoundExceptionTest() throws Exception {
        //given
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto(1L, "test");

        //when, then
        mockMvc.perform(post("/api/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(writeCommentRequestDto)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(401))
                .andExpect(jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("memberId로 회원이 작성한 댓글을 조회한다.")
    public void readCommentByMemberIdTest() throws Exception {
        //given
        String accessToken = logIn();
        CommentPageInfoRequestDto commentPageInfoRequestDto = new CommentPageInfoRequestDto(0, 10);

        //when //then
        mockMvc.perform(get("/api/comment/member").header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(commentPageInfoRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.readCommentResponseDto[0].title").value("title"))
                .andExpect(jsonPath("$.result.data.readCommentResponseDto[0].content").value("testComment"))
                .andDo(print());
    }

    @Test
    @DisplayName("게시물의 id로 회원이 작성한 댓글을 조회한다.")
    public void readCommentByPostIdTest() throws Exception {
        //given
        String accessToken = logIn();
        CommentPageInfoRequestDto commentPageInfoRequestDto = new CommentPageInfoRequestDto(0, 10);
        ReadCommentByPostRequestDto readCommentByPostRequestDto = new ReadCommentByPostRequestDto(1L, commentPageInfoRequestDto);

        //when //then
        mockMvc.perform(get("/api/comment/post").header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(readCommentByPostRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.readCommentResponseDto[0].title").value("title"))
                .andExpect(jsonPath("$.result.data.readCommentResponseDto[0].content").value("testComment"))
                .andDo(print());
    }

    @Test
    @DisplayName("댓글을 수정하려는 사람과 댓글을 작성한 사람이 일치할 경우 댓글을 변경한다.")
    public void editCommentTest() throws Exception{
        //given
        String accessToken = logIn();
        EditCommentRequestDto editCommentRequestDto = new EditCommentRequestDto(1L, "modifyContent");

        //when //then
        mockMvc.perform(patch("/api/comment/edit").header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(editCommentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data.id").value(1))
                .andExpect(jsonPath("$.result.data.content").value("modifyContent"))
                .andExpect(jsonPath("$.result.data.nickname").value("test"));
    }

    @Test
    @DisplayName("댓글을 작성한 사람과 삭제하려는 사람이 같으면 댓글을 삭제한다.")
    void deleteCommentTest() throws Exception{
        //given
        String accessToken = logIn();
        DeleteCommentRequestDto deleteCommentRequestDto = new DeleteCommentRequestDto(1L);

        //when //then
        mockMvc.perform(delete("/api/comment").header("Authorization", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(deleteCommentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.result.data").value("삭제 완료"))
                .andDo(print());
    }

    private String makeJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private String logIn() {
        LogInRequestDto logInRequestDto = LogInRequestDto.builder()
                .username("test")
                .password("test")
                .build();
        return authService.logIn(logInRequestDto).getAccessToken();
    }
}
