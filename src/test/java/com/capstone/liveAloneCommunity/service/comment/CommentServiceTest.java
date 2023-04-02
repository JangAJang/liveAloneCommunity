package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.comment.WriteCommentRequestDto;
import com.capstone.liveAloneCommunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.capstone.liveAloneCommunity.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.Random;
import java.util.stream.IntStream;
import static com.capstone.liveAloneCommunity.domain.post.Category.COOKING;
import static com.capstone.liveAloneCommunity.domain.post.Category.HOBBY_SHARE;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void initTestData() {
        IntStream.range(0, 10).forEach(i -> {
            authService.register(RegisterRequestDto.builder()
                    .username("test" + i)
                    .nickname("test" + i)
                    .email("test" + i + "@test.com")
                    .password("test")
                    .passwordCheck("test")
                    .build());
            Member member = memberRepository.findByUsername_Username("test" + i).orElseThrow(MemberNotFoundException::new);
            IntStream.range(i * 10, i * 10 + 5).forEach(index ->
            {
                postService.writePost(member, WritePostRequestDto.builder()
                        .category(COOKING)
                        .title("title" + index)
                        .content("content" + index).build());
                Post post = postRepository.findByTitle_Title("title" + index)
                        .orElseThrow(PostNotFoundException::new);
                IntStream.range(0, 100).forEach(comment ->
                {
                    commentService.writeComment(new WriteCommentRequestDto(post.getId(), "commentTest" + comment), member);
                });
            });
            IntStream.range(i * 10 + 5, i * 10 + 10).forEach(index ->
            {
                postService.writePost(member, WritePostRequestDto.builder()
                        .category(HOBBY_SHARE)
                        .title("title" + index)
                        .content("content" + index).build());
                Post post = postRepository.findByTitle_Title("title" + index)
                        .orElseThrow(PostNotFoundException::new);
                IntStream.range(0, 100).forEach(comment ->
                {
                    commentService.writeComment(new WriteCommentRequestDto(post.getId(), "commentTest" + comment), member);
                });
            });
        });
    }

    @Test
    @DisplayName("게시물의 id와 댓글 내용으로 댓글을 생성한다. ")
    void createComment() {
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        Random random = new Random();
        Post post = postRepository.findById(random.nextLong(0, 99))
                .orElseThrow(PostNotFoundException::new);
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto(post.getId(), "testCase~~!!");

        //when
        commentService.writeComment(writeCommentRequestDto, member);

        //then
        assertThat(commentRepository.searchCommentByPostId(post.getId()).size())
                .isEqualTo(101);
    }
}

