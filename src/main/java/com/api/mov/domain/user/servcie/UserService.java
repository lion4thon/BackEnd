package com.api.mov.domain.user.servcie;

import com.api.mov.domain.user.web.dto.SignUpUserReq;

public interface UserService {
    void signUp(SignUpUserReq signUpUserReq);
}
