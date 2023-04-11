package com.capstone.liveAloneCommunity.repository.comment;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.config.querydsl.QuerydslConfig;
import com.capstone.liveAloneCommunity.domain.member.Email;
import com.capstone.liveAloneCommunity.domain.member.Nickname;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.context.annotation.Import;

import static com.capstone.liveAloneCommunity.domain.post.Category.*;
import static com.capstone.liveAloneCommunity.entity.member.Role.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import({QuerydslConfig.class, DatabaseCleanup.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @AfterEach
    void afterTestCleanData() {
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("댓글이 데이터베이스에 잘 저장되는지 확인한다.")
    void saveCommentTest() {
        //given
        Member member = createMember();
        memberRepository.save(member);
        Post post = createPost(member);
        postRepository.save(post);
        Comment comment = new Comment("comment", post, member);

        //when
        Comment save = commentRepository.save(comment);

        //then
        assertThat(comment.getPost()).isEqualTo(save.getPost());
        assertThat(comment.getMember()).isEqualTo(save.getMember());
    }

    private Member createMember() {
        return Member.builder()
                .username(new Username("test"))
                .nickname(new Nickname("test"))
                .email(new Email("test@email.com"))
                .password(new Password("test"))
                .role(USER)
                .build();
    }

    private Post createPost(Member member) {
        return Post.builder()
                .title(new Title("title"))
                .content(new Content("content"))
                .category(COOKING)
                .member(member)
                .build();
    }
}
