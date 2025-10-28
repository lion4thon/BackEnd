package com.api.mov.domain.user.servcie;

import com.api.mov.domain.user.web.dto.SignInUserReq;
import com.api.mov.domain.user.web.dto.SignInUserRes;
import com.api.mov.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public SignInUserRes signIn(SignInUserReq signInUserReq) {

        //username, password 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signInUserReq.getUsername(), signInUserReq.getPassword());

        //실제 검증 -> 사용자 비밀번호 체크
        // authenticate 메소드가 실행될 때 CustomUserDetailsService에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //인증 정보를 기반으로 JWT 토큰 생성
        SignInUserRes signInUserRes = jwtTokenProvider.createTokens(authentication);
        return signInUserRes;
    }
}
