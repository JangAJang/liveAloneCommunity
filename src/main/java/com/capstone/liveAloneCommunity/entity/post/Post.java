package com.capstone.liveAloneCommunity.entity.post;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.entity.category.Category;
import com.capstone.liveAloneCommunity.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Builder
    public Post(Title title, Content content, Member member, Category category){
        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;
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
