package com.capstone.liveAloneCommunity.entity.category;

import com.capstone.liveAloneCommunity.domain.post.Content;
import com.capstone.liveAloneCommunity.domain.post.Title;
import com.capstone.liveAloneCommunity.dto.category.CategoryRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    public Category(Title title, Content content, Category parent) {
        this.title = title;
        this.content = content;
        this.parent = parent;
    }

    public void edit(CategoryRequestDto categoryRequestDto){
        this.title = new Title((categoryRequestDto.getTitle()));
        this.content = new Content((categoryRequestDto.getContent()));
    }
}
