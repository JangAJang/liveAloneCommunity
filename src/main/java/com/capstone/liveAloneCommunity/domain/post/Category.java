package com.capstone.liveAloneCommunity.domain.post;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Category {
    //요리, 취미공유, 분실, 동네 정보공유(질문, 사진관, 사건 사고), 1인 가구 소식(뉴스, 정책)
    COOKING("요리"),
    HOBBY_SHARE("취미 공유"),
    LOST("분실"),
    VILLAGE_INFO("동네 정보공유"),
    SINGLE_NEWS("1인 가구 소식"),
    REQUESTS("건의사항");

    private final String description;

    private Category(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @JsonValue
    public String getName(){
        return name();
    }

    @JsonCreator
    public static Category fromJson(String name){
        return valueOf(name);
    }
}
