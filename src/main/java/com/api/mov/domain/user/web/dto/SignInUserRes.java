package com.api.mov.domain.user.web.dto;

public record SignInUserRes(
        String grantType,
        String accessToken,
        String refreshToken
) {
}
