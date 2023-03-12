package com.capstone.liveAloneComunity.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response {
    private boolean success;
    private int code;
    private Result result;

    public static Response success() { // 4
        return new Response(true, 200, null);
    }

    public static <T> Response success(T data){ return new Response(true, 200, new Success<>(data));}

    public static Response failure(int code, String message){ return new Response(false, code, new Failure(message));}
}
