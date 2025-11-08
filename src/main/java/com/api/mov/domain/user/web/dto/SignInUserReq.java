package com.api.mov.domain.user.web.dto;

import lombok.Getter;
import lombok.Setter;

//회원 로그인
@Getter
@Setter
public class SignInUserReq {
    private String username;
    private String password;
}
