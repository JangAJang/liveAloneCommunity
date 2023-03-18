package com.capstone.liveAloneComunity.controller.auth;

import com.capstone.liveAloneComunity.dto.auth.RegisterRequestDto;
import com.capstone.liveAloneComunity.response.Response;
import com.capstone.liveAloneComunity.service.AuthService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "회원가입", notes = "회원가입 정보를 기입하면 해당 데이터를 파라미터로 Member를 저장한다.")
    public Response register(@RequestBody @Valid RegisterRequestDto registerRequestDto){
        authService.register(registerRequestDto);
        return Response.success();
    }
}
