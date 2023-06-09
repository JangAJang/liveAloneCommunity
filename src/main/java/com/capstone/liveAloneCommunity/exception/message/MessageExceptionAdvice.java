package com.capstone.liveAloneCommunity.exception.message;

import com.capstone.liveAloneCommunity.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MessageExceptionAdvice {

    @ExceptionHandler(MessageNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response MessageNotFoundException() {
        return Response.failure(404, "해당 쪽지를 찾을 수 없습니다.");
    }

    @ExceptionHandler(CanNotSameReceiverAndSenderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response CanNotSameReceiverAndSenderException() {
        return Response.failure(400, "송신자와 수신자는 같을 수 없습니다.");
    }

    @ExceptionHandler(SenderAndMemberNotEqualsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response SenderAndMemberNotEqualsException() {
        return Response.failure(400, "송신자의 정보가 일치하지 않습니다.");
    }

    @ExceptionHandler(NotMyMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response NotMyMessageException() {
        return Response.failure(400, "권한이 없는 쪽지입니다.");
    }

    @ExceptionHandler(DeletedMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response DeletedMessageException() {
        return Response.failure(400, "삭제된 쪽지입니다.");
    }
}
