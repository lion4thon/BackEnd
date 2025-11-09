package com.api.mov.global.response.code.pass;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassErrorResponseCode implements BaseResponseCode {
    PASS_NOT_FOUND_404("PASS_NOT_FOUND_404",404,"해당 패키지를 찾을 수 없습니다."),
    INVALID_PASS_REQUEST_400("INVALID_PASS_REQUEST_400",400, "존재하지 않거나 유효하지 않은 패키지 요청입니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
