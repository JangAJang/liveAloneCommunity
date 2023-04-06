package com.capstone.liveAloneCommunity.controller.email;

import com.capstone.liveAloneCommunity.dto.email.EmailAuthRequestDto;
import com.capstone.liveAloneCommunity.dto.email.EmailAuthValidateRequestDto;
import com.capstone.liveAloneCommunity.exception.email.EmailNotSentException;
import com.capstone.liveAloneCommunity.repository.email.EmailAuthRepository;
import com.capstone.liveAloneCommunity.service.email.EmailAuthService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.capstone.liveAloneCommunity.service.email.EmailAuthComponent.SENDER;

@SpringBootTest
@AutoConfigureMockMvc
public class VerifyEmailAuthTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private EmailAuthService emailAuthService;
    @Autowired
    private EmailAuthRepository emailAuthRepository;

    @BeforeEach
    void putTestData(){
        emailAuthRepository.deleteAll();
        emailAuthService.sendEmail(new EmailAuthRequestDto(SENDER.getValue()));
    }

    private String getEmailAuth(){
        return emailAuthRepository.findByEmail_Email(SENDER.getValue()).orElseThrow(EmailNotSentException::new)
                .getAuthNum();
    }

    @Test
    @DisplayName("이메일 검증에 성공하면 200코드를 반환한다.")
    public void verifyTest_Success() throws Exception{
        //given
        EmailAuthValidateRequestDto emailAuthValidateRequestDto = new EmailAuthValidateRequestDto(SENDER.getValue(), getEmailAuth());
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/email/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(emailAuthValidateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andDo(MockMvcResultHandlers.print());
    }

    private String makeJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
