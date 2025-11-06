package com.api.mov.global.response.code.user;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserErrorResponseCode implements BaseResponseCode {
    DUPLICATE_USERNAME_409("DUPLICATE_USERNAME_409", 409, "사용자 이름이 이미 존재합니다."),
    USER_NOT_FOUND_404("USER_NOT_FOUND_404",404,"해당 유저를 찾을 수 없습니다.");



    private final String code;
    private final int httpStatus;
    private final String message;
}
