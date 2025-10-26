package com.api.mov.domain.user.servcie;

import com.api.mov.domain.user.web.dto.UserSignUpReq;

public interface UserService {
    void signUp(UserSignUpReq userSignUpReq);
}
