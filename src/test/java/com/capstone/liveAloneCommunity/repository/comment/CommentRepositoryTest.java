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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.stream.IntStream;

import static com.capstone.liveAloneCommunity.domain.post.Category.*;
import static com.capstone.liveAloneCommunity.entity.member.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.domain.Sort.Direction.DESC;

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

    @Test
    @DisplayName("member 정보로 회원이 작성한 댓글들을 조회한다.")
    void findCommentById (){
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        memberRepository.save(member1);
        memberRepository.save(member2);
        Post post = createPost(member1);
        postRepository.save(post);
        IntStream.range(0, 12).forEach(i -> {
            commentRepository.save(new Comment("test" + i, post, member1));
            commentRepository.save(new Comment("Test" + i, post, member2));
        });
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(DESC, "createdDate"));

        //when
        Page<Comment> commentByMember = commentRepository.findCommentByMember(member1, pageRequest);

        //then
        assertThat(commentByMember.getContent().size()).isEqualTo(2);
        assertThat(commentByMember.getContent().get(0).getContent()).isEqualTo("test1");
        assertThat(commentByMember.getContent().get(1).getContent()).isEqualTo("test0");
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
