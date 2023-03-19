package com.capstone.liveAloneComunity.entity.post;

import com.capstone.liveAloneComunity.domain.post.Content;
import com.capstone.liveAloneComunity.domain.post.Title;
import com.capstone.liveAloneComunity.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder
    public Post(Title title, Content content, Member member){
        this.title = title;
        this.content = content;
        this.member = member;
        member.addPost(this);
    }

    public String getTitle(){
        return title.getTitle();
    }

    public String getContent(){
        return content.getContent();
    }

    public void editTitle(Title title){
        this.title = title;
    }

    public void editContent(Content content){
        this.content = content;
    }

    public boolean isWriter(Member member){
        return this.member.equals(member);
    }

    public String getWritersName(){
        return member.getNickname();
    }
}
