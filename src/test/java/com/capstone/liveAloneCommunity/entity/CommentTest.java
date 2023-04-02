package com.capstone.liveAloneCommunity.entity;

import com.capstone.liveAloneCommunity.domain.member.MemberInfo;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import com.capstone.liveAloneCommunity.entity.post.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class CommentTest {

    static Member member;
    static Post post;

    @BeforeEach
    void initTestData() {
         member = new Member(new Username("test1"), new MemberInfo("test2", "email@email.com"),
                new Password("test3"), Role.USER);
         post = new Post(new Title("title"), new Content("content"), member, Category.COOKING);
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
    @DisplayName("댓글을 작성했을 때 댓글 작성자를 반환한다.")
    void getWriteNameTest() {
        //given

        //when
        Comment comment = new Comment("test", post, member);

        //then
        assertThat(comment.getWriterName()).isEqualTo(member.getNickname());
    }
}
