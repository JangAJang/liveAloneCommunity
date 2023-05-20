package com.capstone.liveAloneCommunity.repository.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SearchMessageType {

    NAME, CONTENT, CALENDER;

    @JsonValue
    public String getName(){
        return name();
    }

    @JsonCreator
    public static SearchMessageType fromJson(String name){
        return valueOf(name);
    }
}
