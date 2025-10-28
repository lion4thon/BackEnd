package com.api.mov.domain.user.web.dto;

import lombok.Getter;


//회원가입
@Getter
public class SignUpUserReq {
    private String username;
    private String password;
}
