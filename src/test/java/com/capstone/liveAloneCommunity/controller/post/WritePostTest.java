package com.capstone.liveAloneCommunity.controller.post;

import com.capstone.liveAloneCommunity.DatabaseCleanup;
import com.capstone.liveAloneCommunity.domain.post.Category;
import com.capstone.liveAloneCommunity.dto.auth.LogInRequestDto;
import com.capstone.liveAloneCommunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneCommunity.dto.post.WritePostRequestDto;
import com.capstone.liveAloneCommunity.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

@SpringBootTest
@AutoConfigureMockMvc
public class WritePostTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void initData(){
        authService.register(RegisterRequestDto.builder()
                .username("test")
                .nickname("test")
                .email("test@test.com")
                .passwordCheck("test")
                .password("test").build());
    }

    @AfterEach
    void clearDB(){
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("토큰이 있고, 입력이 올바르게 왔을 경우 200코드와 작성된 게시물이 반환된다.")
    public void writePostTest_Success() throws Exception{
        //given
        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .category(Category.COOKING)
                .title("제목")
                .content("내용").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/post/write")
                .header("Authorization", getAccessTokenAfterLogIn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(writePostRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.writer").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.title").value("제목"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.content").value("내용"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.categoryName").value("COOKING"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.data.createdDate").isString())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("토큰이 없을 때, 401코드와 다시 로그인해야함을 알려준다.")
    public void writePostTest_Fail_Unauthorized() throws Exception{
        //given
        WritePostRequestDto writePostRequestDto = WritePostRequestDto.builder()
                .category(Category.COOKING)
                .title("제목")
                .content("내용").build();
        //expected
        mvc.perform(MockMvcRequestBuilders.post("/api/post/write")
                .contentType(MediaType.APPLICATION_JSON)
                .content(makeJson(writePostRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(401))
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.failMessage").value("다시 로그인해주세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    private String getAccessTokenAfterLogIn(){
        return authService.logIn(LogInRequestDto.builder().username("test").password("test").build()).getAccessToken();
    }

    private String makeJson(Object object)throws Exception{
        return new ObjectMapper().writeValueAsString(object);
    }
}
