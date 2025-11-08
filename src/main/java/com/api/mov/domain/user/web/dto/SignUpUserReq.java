package com.api.mov.domain.user.web.dto;

import lombok.Getter;
import lombok.Setter;


//회원가입
@Getter
@Setter
public class SignUpUserReq {
    private String username;
    private String password;
}
