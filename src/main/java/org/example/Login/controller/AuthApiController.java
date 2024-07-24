package org.example.Login.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.Login.dto.CustomUserInfoDto;
import org.example.Login.dto.LoginRequestDto;
import org.example.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {
    private final JwtUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<String> getMemberProfile(
            @Valid @RequestBody LoginRequestDto request
    ) {
        System.out.println("Login >");
        System.out.println(request);
        CustomUserInfoDto info = CustomUserInfoDto.builder()
                .memberId(223599L)
                .email(request.getEmail())
                .build();

        String accessToken = jwtUtil.createAccessToken(info);
        System.out.println(accessToken);

        ResponseEntity res = ResponseEntity.status(HttpStatus.OK).body(accessToken);
        System.out.println(res);
        return res;
    }

    @PostMapping("test")
    public ResponseEntity<String> testApi(
            @Valid @RequestBody LoginRequestDto request
    ) {
        System.out.println("Login  test>");
        System.out.println(request);

        return ResponseEntity.status(HttpStatus.OK).body("DONE");
    }

    @PostMapping("no-need-login")
    public ResponseEntity<String> noNeedLogin(
            @Valid @RequestBody LoginRequestDto request
    ) {
        System.out.println("Login >");
        System.out.println(request);

        return ResponseEntity.status(HttpStatus.OK).body("DONE");
    }
}