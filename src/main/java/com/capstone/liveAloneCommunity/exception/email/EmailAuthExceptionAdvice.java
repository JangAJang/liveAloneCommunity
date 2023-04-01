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
        return Response.failure(400, "인증번호가 일치하지 않습니다..");
    }
}
