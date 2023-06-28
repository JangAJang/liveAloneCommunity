package com.capstone.liveAloneCommunity.dto.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public enum MemberSearchType{

    NICKNAME, EMAIL, USERNAME;

    @JsonValue
    public String getName(){
        return name();
    }

    @JsonCreator
    public static MemberSearchType fromJson(final String name) {
        return valueOf(name);
    }
}