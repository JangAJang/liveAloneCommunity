package com.capstone.liveAloneComunity.entity.category;

import com.capstone.liveAloneComunity.domain.post.Content;
import com.capstone.liveAloneComunity.domain.post.Title;
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

    public Category(Title title, Content content) {
        this.title = title;
        this.content = content;
    }
}
