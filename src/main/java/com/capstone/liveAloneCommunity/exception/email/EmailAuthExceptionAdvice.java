package com.capstone.liveAloneCommunity.exception.email;

import com.capstone.liveAloneCommunity.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EmailAuthExceptionAdvice {

    @ExceptionHandler(EmailNotSentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response emailNotSentException(){
        return Response.failure(404, "이메일 인증 요청을 한 적이 없습니다. 인증 요청해주세요.");
    }

    @ExceptionHandler(EmailAuthNotEqualException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response emailAuthNotEqualException(){
        return Response.failure(400, "인증번호가 일치하지 않습니다.");
    }

    @ExceptionHandler(TryToSendEmailAgain.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response tryToSendEmailAgain(){
        return Response.failure(400, "인증번호 전송이 정상적으로 동작되지 않았습니다. 다시 시도해주세요.");
    }
}
