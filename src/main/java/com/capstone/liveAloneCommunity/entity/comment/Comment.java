package com.capstone.liveAloneCommunity.entity.comment;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.entity.BaseTimeEntity;
import com.capstone.liveAloneCommunity.entity.member.Member;
import com.capstone.liveAloneCommunity.entity.post.Post;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Comment(final String content, final Post post, final Member member) {
        this.content = new Content(content);
        this.post = post;
        this.member = member;
    }

    public String getContent() {
        return content.getContent();
    }

    public String getWriterName() {
        return member.getNickname();
    }

    public String getPostTitle() {
        return post.getTitle();
    }

    public void editContent(final Content modifyContent) {
        this.content = modifyContent;
    }

    public boolean isWriter(final Member member) {
        return this.member.equals(member);
    }
}
