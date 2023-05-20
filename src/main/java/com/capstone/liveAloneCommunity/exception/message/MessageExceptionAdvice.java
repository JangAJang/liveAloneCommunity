package com.capstone.liveAloneCommunity.exception.message;

import com.capstone.liveAloneCommunity.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MessageExceptionAdvice {

    @ExceptionHandler(CanNotSameReceiverAndSenderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response CanNotSameReceiverAndSenderException() {
        return Response.failure(400, "송신자와 수신자는 같을 수 없습니다.");
    }
}
