package com.capstone.liveAloneCommunity.entity.post;

import com.capstone.liveAloneCommunity.domain.location.Location;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.entity.BaseTimeEntity;
import com.capstone.liveAloneCommunity.entity.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.locationtech.jts.geom.Point;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @Embedded
    private Location location;

    @Builder
    @SneakyThrows
    public Post(Title title, Content content, Member member, Category category){
        this.title = title;
        this.content = content;
        this.member = member;
        this.category = category;
        this.location = member.getLocation();
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
