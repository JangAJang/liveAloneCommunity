package com.capstone.liveAloneCommunity.config.jwt;

import com.capstone.liveAloneCommunity.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LocalDateTime now = LocalDateTime.now();
        log.error("Unauthorized request : time : " + now + "\nrequest : " + request + "\nException : " + authException.getMessage());
        response.sendError(401);
        response.getWriter().write(makeResponseToString());
    }

    private String makeResponseToString() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Response.failure(401, "다시 로그인해주세요."));
    }
}
