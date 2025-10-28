package com.api.mov.domain.user.web.dto;

import lombok.Getter;

//회원 로그인
@Getter
public class SignInUserReq {
    private String username;
    private String password;
}
