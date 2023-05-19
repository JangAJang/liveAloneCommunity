package com.capstone.liveAloneCommunity.repository.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ReadMessageType {

    SENDER, RECEIVER, ALL, NOT;

    @JsonValue
    public String getName(){
        return name();
    }

    @JsonCreator
    public static ReadMessageType fromJson(String type){
        return valueOf(type);
    }
}
