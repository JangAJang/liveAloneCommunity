package com.capstone.liveAloneCommunity.repository.comment;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.config.querydsl.QuerydslConfig;
import com.capstone.liveAloneCommunity.domain.member.Email;
import com.capstone.liveAloneCommunity.domain.member.Nickname;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.entity.comment.Comment;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
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

import java.util.List;
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
        Post post = createPost(member);
        Comment comment = new Comment("comment", post, member);

        //when
        Comment save = commentRepository.save(comment);

        //then
        assertThat(comment.getPost()).isEqualTo(save.getPost());
        assertThat(comment.getMember()).isEqualTo(save.getMember());
        assertThat(comment.getContent()).isEqualTo("comment");
    }

    @Test
    @DisplayName("member id로 회원이 작성한 댓글들을 조회한다.")
    void findCommentByMemberIdTest(){
        //given
        Member member1 = createMember();
        Member member2 = createMember();
        Post post = createPost(member1);
        IntStream.range(0, 12).forEach(i -> {
            commentRepository.save(new Comment("test" + i, post, member1));
            commentRepository.save(new Comment("Test" + i, post, member2));
        });
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(DESC, "createdDate"));

        //when
        Page<Comment> commentByMember = commentRepository.findCommentByMemberId(member1.getId(), pageRequest);

        //then
        assertThat(commentByMember.getContent().size()).isEqualTo(2);
        assertThat(commentByMember.getContent().get(0).getContent()).isEqualTo("test1");
        assertThat(commentByMember.getContent().get(1).getContent()).isEqualTo("test0");
    }

    @Test
    @DisplayName("게시물의 id로 회원이 작성한 댓글을 조회한다.")
    public void findCommentByPostIdTest(){
        //given
        Member member = createMember();
        Post post1 = createPost(member);
        Post post2 = createPost(member);
        IntStream.range(0, 15).forEach(i -> {
            commentRepository.save(new Comment("test" + i, post1, member));
            commentRepository.save(new Comment("Test" + i, post2, member));
        });
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(DESC, "createdDate"));

        //when
        Page<Comment> commentByPostId = commentRepository.findCommentByPostId(1L, pageRequest);

        //then
        assertThat(commentByPostId.getContent().size()).isEqualTo(5);
        assertThat(commentByPostId.getContent().get(0).getContent()).isEqualTo("test4");
        assertThat(commentByPostId.getContent().get(4).getContent()).isEqualTo("test0");
    }

    @Test
    @DisplayName("게시물의 id로 게시물의 댓글들을 모두 조회한다.")
    void searchCommentByPostIdTest() {
        //given
        Member member = createMember();
        Post post1 = createPost(member);
        Post post2 = createPost(member);
        IntStream.range(0, 100).forEach(i -> {
            commentRepository.save(new Comment("post1 comment" + i, post1, member));
            commentRepository.save(new Comment("post2 comment" + i, post2, member));
        });

        //when
        List<Comment> comments = commentRepository.searchCommentByPostId(1L);

        //then
        assertThat(comments.size()).isEqualTo(100);
        assertThat(comments.get(0).getContent()).isEqualTo("post1 comment0");
    }

    private Member createMember() {
        return memberRepository.save(Member.builder()
                .username(new Username("test"))
                .nickname(new Nickname("test"))
                .email(new Email("test@email.com"))
                .password(new Password("test"))
                .role(USER)
                .build());
    }

    private Post createPost(Member member) {
        return postRepository.save(Post.builder()
                .title(new Title("title"))
                .content(new Content("content"))
                .category(COOKING)
                .member(member)
                .build());
    }
}
