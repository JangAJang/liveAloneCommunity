package com.capstone.liveAloneComunity.service.post;

import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.dto.category.CategoryRequestDto;
import com.capstone.liveAloneComunity.dto.post.*;
import com.capstone.liveAloneComunity.entity.category.Category;
import com.capstone.liveAloneComunity.entity.member.Member;
import com.capstone.liveAloneComunity.entity.post.Post;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneComunity.repository.category.CategoryRepository;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import com.capstone.liveAloneComunity.repository.post.PostRepository;
import com.capstone.liveAloneComunity.repository.post.SearchPostType;
import com.capstone.liveAloneComunity.service.auth.AuthService;
import com.capstone.liveAloneComunity.service.category.CategoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

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
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void initTestData(){
        categoryService.createCategory(new CategoryRequestDto("category", "categoryDescription"), java.util.Optional.empty());
        Category category = categoryRepository.findByTitle_Title("category").orElseThrow(IllegalAccessError::new);
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
                            .categoryId(category.getId())
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
        Category category = categoryRepository.findByTitle_Title("category").orElseThrow(IllegalAccessError::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(category.getId(), "title", "content");
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
        SearchPostRequestDto searchPostRequestDto = new SearchPostRequestDto("title", SearchPostType.TITLE);
        //when
        MultiPostResponseDto multiPostResponseDto = postService.searchPost(searchPostRequestDto, PageRequest.of(0, 10));
        //then
        Assertions.assertThat(multiPostResponseDto.getResult().getContent().stream().map(PostResponseDto::getTitle).toList())
                .containsExactly("title95", "title94", "title93", "title92", "title91",
                        "title85", "title84", "title83", "title82", "title81");
    }

    @Test
    @DisplayName("게시물을 검색할 때, 내용을 이용해 검색하면, 이에 대한 결과가 이름의 역순으로 페이징처리되어 반환된다.")
    public void searchPostTest_CONTENT() throws Exception{
        //given
        SearchPostRequestDto searchPostRequestDto = new SearchPostRequestDto("1", SearchPostType.CONTENT);
        //when
        MultiPostResponseDto multiPostResponseDto = postService.searchPost(searchPostRequestDto, PageRequest.of(0, 10));
        //then
        Assertions.assertThat(multiPostResponseDto.getResult().getContent().stream().map(PostResponseDto::getTitle).toList())
                .containsExactly("title91", "title81", "title71", "title61", "title51",
                        "title41", "title31", "title21", "title15", "title14");
    }

    @Test
    @DisplayName("게시물을 검색할 때, 작성자를 이용해 검색하면, 이에 대한 결과가 이름의 역순으로 페이징처리되어 반환된다.")
    public void searchPostTest_WRITER() throws Exception{
        //given
        SearchPostRequestDto searchPostRequestDto = new SearchPostRequestDto("1", SearchPostType.WRITER);
        //when
        MultiPostResponseDto multiPostResponseDto = postService.searchPost(searchPostRequestDto, PageRequest.of(0, 10));
        //then
        Assertions.assertThat(multiPostResponseDto.getResult().getContent().stream().map(PostResponseDto::getTitle).toList())
                .containsExactly("title15", "title14", "title13", "title12", "title11",
                        "title105", "title104", "title103", "title102", "title101");
    }

    @Test
    @DisplayName("회원의 아이디를 입력하고 해당 회원의 게시물을 조회하면, 제목의 역순으로 반환된다.")
    public void getMembersPostTest() throws Exception{
        //given
        Long memberId = 1L; // test1
        //when
        MultiPostResponseDto membersPost = postService.getMembersPost(PageRequest.of(0, 10), memberId);
        //then
        Assertions.assertThat(membersPost.getResult().getContent().stream().map(PostResponseDto::getTitle))
                .containsExactly("title15", "title14", "title13", "title12", "title11");
    }

    @Test
    @DisplayName("회원의 게시물을 수정할 때, 작성한 회원이 게시물을 수정하면 제목과 내용을 초기화해준다.")
    public void editPostTest() throws Exception{
        //given
        EditPostRequestDto editPostRequestDto = new EditPostRequestDto("newT", "newC");
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .password("test")
                .passwordCheck("test").build());
        Member member = memberRepository.findByUsername_Username("test").orElseThrow(MemberNotFoundException::new);
        Category category = categoryRepository.findByTitle_Title("category").orElseThrow(IllegalAccessError::new);
        WritePostRequestDto writePostRequestDto = new WritePostRequestDto(category.getId(), "title", "content");
        postService.writePost(member, writePostRequestDto);
        Post post = postRepository.findByTitle_Title("title").orElseThrow(PostNotFoundException::new);
        //when
        postService.editPost(editPostRequestDto, member, post.getId());
        //then
        Assertions.assertThat(post.getTitle()).isEqualTo(editPostRequestDto.getTitle());
        Assertions.assertThat(post.getContent()).isEqualTo(editPostRequestDto.getContent());
        Assertions.assertThat(postRepository.findByTitle_Title("newT").orElseThrow(PostNotFoundException::new).getContent())
                .isEqualTo("newC");
    }
}
