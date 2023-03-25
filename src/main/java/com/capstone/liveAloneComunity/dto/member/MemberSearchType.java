package com.capstone.liveAloneComunity.dto.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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
    public static MemberSearchType fromJson(String name){
        return valueOf(name);
    }
}
