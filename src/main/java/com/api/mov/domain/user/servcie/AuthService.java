package com.api.mov.domain.user.servcie;

import com.api.mov.domain.user.web.dto.SignInUserReq;
import com.api.mov.domain.user.web.dto.SignInUserRes;

public interface AuthService {
    SignInUserRes signIn(SignInUserReq signInUserReq);
}
