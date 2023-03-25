package com.capstone.liveAloneCommunity.exception.category;

import com.capstone.liveAloneCommunity.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoryExceptionAdvice {

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException(){
        return Response.failure(404, "해당 카테고리를 찾을 수 없습니다.");
    }

}
