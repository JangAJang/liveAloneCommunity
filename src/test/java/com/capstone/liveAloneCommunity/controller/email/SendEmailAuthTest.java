package com.capstone.liveAloneCommunity.controller.email;

import com.capstone.liveAloneCommunity.dto.email.EmailAuthRequestDto;
import com.capstone.liveAloneCommunity.repository.email.EmailAuthRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.capstone.liveAloneCommunity.service.email.EmailAuthComponent.SENDER;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SendEmailAuthTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @BeforeEach
    void clearDB(){
        emailAuthRepository.deleteAll();
    }

    @Test
    @DisplayName("이메일을 성공적으로 전송하면 EmailAuth를 반환한다.")
    public void sendEmailAuthTest() throws Exception{
        //given
        EmailAuthRequestDto emailAuthRequestDto = new EmailAuthRequestDto(SENDER.getValue());
        //expected
        mvc.perform(post("/api/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(emailAuthRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result.data.authNum").isString())
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 형식이 올바르지 않을 때, 400에러와 \"올바르지 않은 이메일 형식입니다.\"를 반환한다.")
    public void sendEmailAuthTest_Email_Not_Format() throws Exception{
        //given
        EmailAuthRequestDto emailAuthRequestDto = new EmailAuthRequestDto("wrong");
        //expected
        mvc.perform(post("/api/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(makeJson(emailAuthRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.result.failMessage").value("올바르지 않은 이메일 형식입니다."))
                .andDo(print());
    }

    private String makeJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
