package com.capstone.liveAloneComunity.service.post;

import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.dto.category.CategoryRequestDto;
import com.capstone.liveAloneComunity.dto.post.PostResponseDto;
import com.capstone.liveAloneComunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneComunity.entity.category.Category;
import com.capstone.liveAloneComunity.entity.member.Member;
import com.capstone.liveAloneComunity.exception.member.MemberNotFoundException;
import com.capstone.liveAloneComunity.exception.post.PostNotFoundException;
import com.capstone.liveAloneComunity.repository.category.CategoryRepository;
import com.capstone.liveAloneComunity.repository.member.MemberRepository;
import com.capstone.liveAloneComunity.repository.post.PostRepository;
import com.capstone.liveAloneComunity.service.auth.AuthService;
import com.capstone.liveAloneComunity.service.category.CategoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}
