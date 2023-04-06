package com.capstone.liveAloneCommunity.controller.email;

import com.capstone.liveAloneCommunity.dto.email.EmailAuthRequestDto;
import com.capstone.liveAloneCommunity.dto.email.EmailAuthValidateRequestDto;
import com.capstone.liveAloneCommunity.response.Response;
import com.capstone.liveAloneCommunity.service.email.EmailAuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailAuthService emailAuthService;

    @Operation(summary = "인증번호 전송", description = "이메일을 입력받아 해당 이메일에게 인증번호를 전송해준다. ")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/send")
    public Response sendEmailAuth(@RequestBody @Valid EmailAuthRequestDto emailAuthRequestDto){
        return Response.success(emailAuthService.sendEmail(emailAuthRequestDto));
    }

    @Operation(summary = "인증번호 확인", description = "이메일로 받은 인증번호가 일치하는지 확인한다. ")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verify")
    public Response verifyEmailAuth(@RequestBody @Valid EmailAuthValidateRequestDto emailAuthRequestDto){
        emailAuthService.verifyEmailAuth(emailAuthRequestDto);
        return Response.success();
    }
}
