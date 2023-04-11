package com.capstone.liveAloneCommunity.entity;

import com.capstone.liveAloneCommunity.domain.member.Email;
import com.capstone.liveAloneCommunity.domain.member.Nickname;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import com.capstone.liveAloneCommunity.entity.post.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.capstone.liveAloneCommunity.domain.post.Category.*;
import static org.assertj.core.api.Assertions.*;

class CommentTest {

    private static Member member;
    private static Post post;

    @BeforeEach
    void initTestData() {
        member = Member.builder()
                .username(new Username("test1"))
                .nickname(new Nickname("test2"))
                .email(new Email("test@email.com"))
                .password(new Password("test3"))
                .role(Role.USER)
                .build();
        post = Post.builder()
                .title(new Title("title"))
                .content(new Content("contetn"))
                .category(COOKING)
                .build();
    }

    @Test
    @DisplayName("댓글을 생성했을 때 댓글의 필드가 초기화된다.")
    void createComment() {
        //given
        //when
        Comment comment = new Comment("test", post, member);

        //then
        assertThat(comment.getContent()).isEqualTo("test");
        assertThat(comment.getPost()).isEqualTo(post);
        assertThat(comment.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("댓글을 작성했을 때 댓글 작성자를 반환할 수 있다.")
    void getWriteNameTest() {
        //given
        Comment comment = new Comment("test", post, member);

        //when
        String writerName = comment.getWriterName();

        //then
        assertThat(writerName).isEqualTo(member.getNickname());
    }

    @Test
    @DisplayName("댓글을 작성했을 때 댓글이 적힌 게시판의 이름을 반환할 수 있다.")
    void getPostTitleTest (){
        //given
        Comment comment = new Comment("test", post, member);

        //when
        String postTitle = comment.getPostTitle();

        //then
        assertThat(postTitle).isEqualTo(post.getTitle());
    }

    @Test
    @DisplayName("수정할 댓글로 댓글의 내용을 수정한다.")
    void editContentTest() {
        //given
        Comment comment = new Comment("test", post, member);
        Content modifyContent = new Content("modifyContent");

        //when
        comment.editContent(modifyContent);

        //then
        assertThat(comment.getContent()).isEqualTo("modifyContent");
    }
}
