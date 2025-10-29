package com.api.mov.domain.user.web.controller;

import com.api.mov.domain.user.servcie.AuthService;
import com.api.mov.domain.user.servcie.UserService;
import com.api.mov.domain.user.web.dto.SignInUserReq;
import com.api.mov.domain.user.web.dto.SignInUserRes;
import com.api.mov.domain.user.web.dto.SignUpUserReq;
import com.api.mov.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<?>> signUp(@RequestBody SignUpUserReq signUpUserReq){
        userService.signUp(signUpUserReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success("회원가입에 성공했습니다"));
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<?>> signIn(@RequestBody SignInUserReq signInUserReq) {
        SignInUserRes tokens = authService.signIn(signInUserReq);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.ok(tokens));
    }
}
