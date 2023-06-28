package com.capstone.liveAloneCommunity.repository.post;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SearchPostType {

    TITLE, TITLE_AND_CONTENT,
    WRITER_AND_TITLE, WRITER,
    CONTENT;

    @JsonValue
    public String getName() {
        return name();
    }

    @JsonCreator
    public static SearchPostType fromJson(final String name) {
        return valueOf(name);
    }
}
