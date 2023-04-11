package com.capstone.liveAloneCommunity.exception.comment;

import com.capstone.liveAloneCommunity.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommentExceptionAdvice {

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response commentNotFoundException() {
        return Response.failure(404, "해당 댓글을 찾을 수 없습니다.");
    }
}
