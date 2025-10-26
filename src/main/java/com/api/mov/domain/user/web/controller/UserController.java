package com.api.mov.domain.user.web.controller;


import com.api.mov.domain.user.servcie.UserService;
import com.api.mov.domain.user.web.dto.UserSignUpReq;
import com.api.mov.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<SuccessResponse<?>> signUp(@RequestBody UserSignUpReq userSignUpReq){
        userService.signUp(userSignUpReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success("회원가입에 성공했습니다"));
    }
}
