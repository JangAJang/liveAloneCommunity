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
    void clearDB(){
        emailAuthRepository.deleteAll();
    }

    private String getEmailAuth(){
        return emailAuthService.sendEmail(new EmailAuthRequestDto(SENDER.getValue())).getAuthNum();
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

    @Test
    @DisplayName("인증번호를 전송한 적 없는 이메일로 인증요청을 할 경우, 인증요청을 다시 하도록 문구를 반환한다.")
    public void verifyTest_Fail_Email_Not_Sent() throws Exception{
        //given
        EmailAuthValidateRequestDto emailAuthValidateRequestDto = new EmailAuthValidateRequestDto(SENDER.getValue(), "testtest");
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/email/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(emailAuthValidateRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(404))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage")
                        .value("이메일 인증 요청을 한 적이 없습니다. 인증 요청해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String makeJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
