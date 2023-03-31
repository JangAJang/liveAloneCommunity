package com.capstone.liveAloneCommunity.service.post;

import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.post.*;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import com.capstone.liveAloneCommunity.exception.member.MemberNotAllowedException;
import com.capstone.liveAloneCommunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneCommunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneCommunity.repository.member.MemberRepository;
import com.capstone.liveAloneCommunity.repository.post.PostRepository;
import com.capstone.liveAloneCommunity.repository.post.SearchPostType;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.capstone.liveAloneCommunity.domain.post.Category.*;

@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private PostService postService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void initTestData(){
        memberRepository.deleteAll();
        postRepository.deleteAll();
        IntStream.range(1, 11).forEach(i -> {
            authService.register(RegisterRequestDto.builder()
                    .username("test" + i)
                    .nickname("test" + i)
                    .email("test" + i + "@test.com")
                    .password("test")
                    .passwordCheck("test").build());
            Member member = memberRepository.findByUsername_Username("test"+i).orElseThrow(MemberNotFoundException::new);
            IntStream.range(i*10+1, i*10+6).forEach(index ->
                    postService.writePost(member, WritePostRequestDto.builder()
                            .category(COOKING)
                            .title("title"+index)
                            .content("content" + index).build()));
        });
    }

    @Test
    @DisplayName("카테고리 id, 제목, 내용을 Member 엔티티와 입력하면 게시물을 생성한다.")
    public void createPostTest() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(COOKING, "title", "content");
        //when
        postService.writePost(member, writePostRequestDto);
        //then
        Assertions.assertThat(postRepository.findByTitle_Title("title").isPresent()).isTrue();
    }

    @Test
    @DisplayName("게시물의 아이디를 입력받으면 게시물 단건을 조회한다.")
    public void getPostTest() throws Exception{
        //given
        Long id = postRepository.findByTitle_Title("title11").orElseThrow(PostNotFoundException::new).getId();
        //when
        PostResponseDto postResponseDto = postService.getPost(id);
        //then
        Assertions.assertThat(postResponseDto.getTitle()).isEqualTo("title11");
    }

    @Test
    @DisplayName("게시물이 존재하지 않으면, PostNotFoundException을 반환한다..")
    public void getPostTest_FAIL() throws Exception{
        //given

        //when

        //then
        Assertions.assertThatThrownBy(() -> postService.getPost(70L))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("게시물을 검색할 때, 제목을 이용해 검색하면, 이에 대한 결과가 이름의 역순으로 페이징처리되어 반환된다.")
    public void searchPostTest_TITLE() throws Exception{
        //given
        SearchPostRequestDto searchPostRequestDto = SearchPostRequestDto.builder()
                .searchPostType(SearchPostType.TITLE)
                .text("title")
                .page(0)
                .size(10).build();
        //when
        MultiPostResponseDto multiPostResponseDto = postService.searchPost(searchPostRequestDto);
        //then
        Assertions.assertThat(multiPostResponseDto.getResult().stream().map(PostResponseDto::getTitle).toList())
                .containsExactly("title95", "title94", "title93", "title92", "title91",
                        "title85", "title84", "title83", "title82", "title81");
    }

    @Test
    @DisplayName("게시물을 검색할 때, 내용을 이용해 검색하면, 이에 대한 결과가 이름의 역순으로 페이징처리되어 반환된다.")
    public void searchPostTest_CONTENT() throws Exception{
        //given
        SearchPostRequestDto searchPostRequestDto = SearchPostRequestDto.builder()
                .searchPostType(SearchPostType.CONTENT)
                .text("1")
                .page(0)
                .size(10).build();
        //when
        MultiPostResponseDto multiPostResponseDto = postService.searchPost(searchPostRequestDto);
        //then
        Assertions.assertThat(multiPostResponseDto.getResult().stream().map(PostResponseDto::getTitle).toList())
                .containsExactly("title91", "title81", "title71", "title61", "title51",
                        "title41", "title31", "title21", "title15", "title14");
    }

    @Test
    @DisplayName("게시물을 검색할 때, 작성자를 이용해 검색하면, 이에 대한 결과가 이름의 역순으로 페이징처리되어 반환된다.")
    public void searchPostTest_WRITER() throws Exception{
        //given
        SearchPostRequestDto searchPostRequestDto = SearchPostRequestDto.builder()
                .searchPostType(SearchPostType.WRITER)
                .text("1")
                .page(0)
                .size(10).build();
        //when
        MultiPostResponseDto multiPostResponseDto = postService.searchPost(searchPostRequestDto);
        //then
        Assertions.assertThat(multiPostResponseDto.getResult().stream().map(PostResponseDto::getTitle).toList())
                .containsExactly("title15", "title14", "title13", "title12", "title11",
                        "title105", "title104", "title103", "title102", "title101");
    }

    @Test
    @DisplayName("회원의 아이디를 입력하고 해당 회원의 게시물을 조회하면, 제목의 역순으로 반환된다.")
    public void getMembersPostTest() throws Exception{
        //given
        Long memberId = memberRepository.findByUsername_Username("test1").orElseThrow(MemberNotFoundException::new).getId();
        //when
        MultiPostResponseDto membersPost = postService.getMembersPost(
                MembersPostRequestDto.builder()
                        .id(memberId)
                        .page(0)
                        .size(10)
                        .build());
        //then
        Assertions.assertThat(membersPost.getResult().stream().map(PostResponseDto::getTitle).collect(Collectors.toList()))
                .containsExactly("title15", "title14", "title13", "title12", "title11");
    }

    @Test
    @DisplayName("회원의 게시물을 수정할 때, 작성한 회원이 게시물을 수정하면 제목과 내용을 초기화해준다.")
    public void editPostTest() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(COOKING, "title", "content");
        postService.writePost(member, writePostRequestDto);
        Post post = postRepository.findByTitle_Title("title").orElseThrow(PostNotFoundException::new);
        EditPostRequestDto editPostRequestDto = EditPostRequestDto.builder()
                .id(post.getId())
                .title("newT")
                .content("newC").build();
        //when
        postService.editPost(editPostRequestDto, member);
        //then
        Assertions.assertThat(post.getTitle()).isEqualTo(editPostRequestDto.getTitle());
        Assertions.assertThat(post.getContent()).isEqualTo(editPostRequestDto.getContent());
        Assertions.assertThat(postRepository.findByTitle_Title("newT").orElseThrow(PostNotFoundException::new).getContent())
                .isEqualTo("newC");
    }

    @Test
    @DisplayName("회원의 게시물을 수정할 때, 존재하지 않는 게시물을 수정하면 PostNotFoundException을 반환한다.")
    public void editPostFail_NotFound() throws Exception{
        //given
        EditPostRequestDto editPostRequestDto = new EditPostRequestDto(100L, "newT", "newC");
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(COOKING, "title", "content");
        postService.writePost(member, writePostRequestDto);
        //when

        //then
        Assertions.assertThatThrownBy(()-> postService.editPost(editPostRequestDto, member))
                .isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("다른 사람이 작성한 게시물을 수정하려하면, MemberNotAllowedException을 반환한다.")
    public void editPostFail_NoAuthority() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(COOKING, "title", "content");
        postService.writePost(member, writePostRequestDto);
        Post post = postRepository.findByTitle_Title("title").orElseThrow(PostNotFoundException::new);
        EditPostRequestDto editPostRequestDto = new EditPostRequestDto(post.getId(), "newT", "newC");
        Member member1 = memberRepository.findByUsername_Username("test1").orElseThrow(MemberNotFoundException::new);
        //when

        //then
        Assertions.assertThatThrownBy(() -> postService.editPost(editPostRequestDto, member1))
                .isInstanceOf(MemberNotAllowedException.class);
    }

    @Test
    @DisplayName("작성한 멤버가 게시물을 삭제하려하면 정상적으로 삭제시킨다.")
    public void deleteTest() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(COOKING, "title", "content");
        postService.writePost(member, writePostRequestDto);
        Post post = postRepository.findByTitle_Title("title").orElseThrow(PostNotFoundException::new);
        //when
        postService.deletePost(post.getId(), member);
        //then
        Assertions.assertThat(postRepository.findById(post.getId()).isPresent()).isFalse();
    }

    @Test
    @DisplayName("게시물을 삭제할 때, 해당 게시물의 소유자가 아니면 MemberNotAllowedException을 반환한다.")
    public void deleteFail() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        Member member1 = memberRepository.findByUsername_Username("test1").orElseThrow(MemberNotFoundException::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(COOKING, "title", "content");
        postService.writePost(member, writePostRequestDto);
        Post post = postRepository.findByTitle_Title("title").orElseThrow(PostNotFoundException::new);
        //when

        //then
        Assertions.assertThatThrownBy(() -> postService.deletePost(post.getId(), member1))
                .isInstanceOf(MemberNotAllowedException.class);
    }

    @Test
    @DisplayName("게시물을 삭제할 때, 존재하지 않는 게시물을 삭제하면 PostNotFoundException을 반환한다.")
    public void deleteFail_PostNotFound() throws Exception{
        //given
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        Member member1 = memberRepository.findByUsername_Username("test1").orElseThrow(MemberNotFoundException::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(COOKING, "title", "content");
        postService.writePost(member, writePostRequestDto);
        Post post = postRepository.findByTitle_Title("title").orElseThrow(PostNotFoundException::new);
        //when

        //then
        Assertions.assertThatThrownBy(() -> postService.deletePost(100L, member))
                .isInstanceOf(PostNotFoundException.class);
    }
}
