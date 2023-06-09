package com.capstone.liveAloneCommunity.entity.message;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.entity.BaseTimeEntity;
import com.capstone.liveAloneCommunity.entity.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Content content;
    private boolean deletedByReceiver = false;
    private boolean deletedBySender = false;
    @ManyToOne
    @JoinColumn(name = "receiver_name")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member receiver;
    @ManyToOne
    @JoinColumn(name = "sender_name")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Member sender;

    public Message(Member sender, Member receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = new Content(content);
    }

    public String getReceiverNickname() {
        return this.receiver.getNickname();
    }

    public String getSenderNickname() {
        return this.sender.getNickname();
    }
    
    public String getContent() {
        return this.content.getContent();
    }

    public void deleteBySender() {
        this.deletedBySender = true;
    }

    public void deleteByReceiver() {
        this.deletedByReceiver = true;
    }

    public boolean isDeletedMessage() {
        return deletedBySender && deletedByReceiver;
    }

    public boolean isReceiver(Member member) {
        return this.receiver.getNickname().equals(member.getNickname());
    }

    public boolean isSender(Member member) {
        return this.sender.getNickname().equals(member.getNickname());
    }
}
