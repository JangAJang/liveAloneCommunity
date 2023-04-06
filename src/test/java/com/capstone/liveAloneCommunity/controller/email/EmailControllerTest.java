package com.capstone.liveAloneCommunity.controller.email;

import com.capstone.liveAloneCommunity.dto.email.EmailAuthRequestDto;
import com.capstone.liveAloneCommunity.repository.email.EmailAuthRepository;
import com.capstone.liveAloneCommunity.service.email.EmailAuthService;
import com.capstone.liveAloneCommunity.service.email.EmailConstructor;
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
public class EmailControllerTest {

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
        mvc.perform(MockMvcRequestBuilders.post("/api/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(emailAuthRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andDo(MockMvcResultHandlers.print());
    }

    private String makeJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
