package com.capstone.liveAloneCommunity.entity;

import com.capstone.liveAloneCommunity.domain.member.MemberInfo;
import com.capstone.liveAloneCommunity.domain.member.Password;
import com.capstone.liveAloneCommunity.domain.member.Username;
import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.entity.category.Category;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.member.Role;
import com.capstone.liveAloneCommunity.entity.post.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostTest {

    @Test
    @DisplayName("게시물을 생성하면, 게시물에 대한 데이터를 초기화시킨다.")
    public void createTest() throws Exception{
        //given
        Category category = new Category(new Title("category"), new Content("description"), null);
        Member member = new Member(new Username("username"), new MemberInfo("nickname", "email@email.com"),
                new Password("p"), Role.USER);
        //when
        Post post = new Post(new Title("title"), new Content("content"), member, category);
        //then
        Assertions.assertThat(post.getTitle()).isEqualTo("title");
        Assertions.assertThat(post.getContent()).isEqualTo("content");
        Assertions.assertThat(post.getMember()).isEqualTo(member);
        Assertions.assertThat(post.getCategory()).isEqualTo(category);
    }

    @Test
    @DisplayName("게시물의 제목을 수정하면 Title인스턴스가 초기화되며 getTitle로 반환된 문자열도 변경된다.")
    public void editTitleTest() throws Exception{
        //given
        Category category = new Category(new Title("category"), new Content("description"), null);
        Member member = new Member(new Username("username"), new MemberInfo("nickname", "email@email.com"),
                new Password("p"), Role.USER);
        Post post = new Post(new Title("title"), new Content("content"), member, category);
        Title newTitle = new Title("newTitle");
        //when
        post.editTitle(newTitle);
        //then
        Assertions.assertThat(post.getTitle()).isEqualTo(newTitle.getTitle());
    }

    @Test
    @DisplayName("게시물의 내용을 수정할 떄, Content도메인을 입력하면 게시물의 인스턴스를 초기화시키고, getContent로 받아오는 문자열도 변경된 값이 나온다.")
    public void editContentTest() throws Exception{
        //given
        Category category = new Category(new Title("category"), new Content("description"), null);
        Member member = new Member(new Username("username"), new MemberInfo("nickname", "email@email.com"),
                new Password("p"), Role.USER);
        Post post = new Post(new Title("title"), new Content("content"), member, category);
        Content content = new Content("newContent");
        //when
        post.editContent(content);
        //then
        Assertions.assertThat(post.getContent()).isEqualTo(content.getContent());
    }

    @Test
    @DisplayName("isWriter 메서드를 통해 해당 게시물을 작성한 멤버일 경우엔 True, 아닐 경우 False를 반환받는다.")
    public void isWriterTest() throws Exception{
        //given
        Category category = new Category(new Title("category"), new Content("description"), null);
        Member member = new Member(new Username("username"), new MemberInfo("nickname", "email@email.com"),
                new Password("p"), Role.USER);
        Member member1 = new Member(new Username("username1"), new MemberInfo("nickname1", "email1@email.com"),
                new Password("p1"), Role.USER);
        Post post = new Post(new Title("title"), new Content("content"), member, category);
        //when
        //then
        Assertions.assertThat(post.isWriter(member)).isTrue();
        Assertions.assertThat(post.isWriter(member1)).isFalse();
    }

    @Test
    @DisplayName("작성한 멤버의 Nickname을 반환받는다.")
    public void getWriterName() throws Exception{
        //given
        Category category = new Category(new Title("category"), new Content("description"), null);
        Member member = new Member(new Username("username"), new MemberInfo("nickname", "email@email.com"),
                new Password("p"), Role.USER);
        Post post = new Post(new Title("title"), new Content("content"), member, category);
        //when
        //then
        Assertions.assertThat(post.getWritersName()).isEqualTo(member.getNickname());
    }
}
