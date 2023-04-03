package com.capstone.liveAloneCommunity.config.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class TokenPath {

    public void putAccessTokenOnHeader(String accessToken, HttpServletResponse response){
        response.addHeader("Authorization", accessToken);
    }

    public String getAccessTokenFromHeader(HttpServletRequest request){
        return request.getHeader("Authorization");
    }
}
