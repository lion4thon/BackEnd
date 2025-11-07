package com.api.mov.global.response.code.user;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorResponseCode implements BaseResponseCode {
    DUPLICATE_USERNAME_409("DUPLICATE_USERNAME_409", 409, "사용자 이름이 이미 존재합니다."),
    NOT_FOUND_USER_404("NOT_FOUND_USER_404",404,"사용자를 찾이 못했습니다.");


    private final String code;
    private final int httpStatus;
    private final String message;
}
