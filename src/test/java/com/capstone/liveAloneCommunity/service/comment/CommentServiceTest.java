package com.capstone.liveAloneCommunity.service.comment;

import com.capstone.liveAloneCommunity.domain.member.Email;
import com.capstone.liveAloneCommunity.domain.member.Nickname;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.dto.comment.*;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.comment.CommentRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.DESC;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("댓글을 작성한다.")
    void writeCommentTest(){
        //given
        Member member = createMember(1);
        Post post = createPost(member, 1);
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto(1L, "title");
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        //when
        CommentResponseDto commentResponseDto = commentService.writeComment(writeCommentRequestDto, member);

        //then
        assertThat(commentResponseDto.getNickname()).isEqualTo("test1");
        assertThat(commentResponseDto.getContent()).isEqualTo("title");
    }

    @Test
    @DisplayName("작성한 댓글의 게시물을 찾지 못하면 에러를 발생시킨다.")
    void postNotFoundExceptionTest() {
        //given
        Member member = createMember(1);
        WriteCommentRequestDto writeCommentRequestDto = new WriteCommentRequestDto(1L, "comment");

        //when //then
        assertThatThrownBy(() -> commentService.writeComment(writeCommentRequestDto, member))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("member의 id로 회원의 댓글을 조회한다.")
    void readCommentByMemberTest() {
        //given
        Member member1 = createMember(1);
        Member member2 = createMember(2);
        Post post = createPost(member2, 1);
        List<Comment> comments = new ArrayList<>();
        IntStream.range(0, 999).forEach(i -> comments.add(createComment(member1, post, i)));
        IntStream.range(0, 888).forEach(i -> comments.add(createComment(member2, post, i)));
        CommentPageInfoRequestDto commentPageInfoRequestDto = new CommentPageInfoRequestDto(0, 10);
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(DESC,"createdDate"));
        Page<Comment> readCommentByMember = new PageImpl<>(comments.stream().filter(comment ->
                comment.getMember().equals(member1)).collect(Collectors.toList()));
        given(commentRepository.findCommentByMember(member1, pageRequest)).willReturn(readCommentByMember);

        //when
        MultiReadCommentResponseDto multiReadCommentResponseDto = commentService.readCommentByMember(member1, commentPageInfoRequestDto);

        //then
        assertThat(multiReadCommentResponseDto.getReadCommentResponseDto().size()).isEqualTo( 999);
    }

    private Member createMember(int id) {
        return Member.builder()
                .username(new Username("test" + id))
                .nickname(new Nickname("test" + id))
                .email(new Email("test" + id + "@email.com"))
                .password(new Password("test" + id))
                .role(Role.USER)
                .build();
    }

    private Post createPost(Member member, int id) {
        return Post.builder()
                .title(new Title("title" + id))
                .content(new Content("content" + id))
                .category(Category.COOKING)
                .member(member)
                .build();
    }
    private Comment createComment(Member member, Post post, int id) {
        return new Comment("comment" + id, post, member);
    }
}
