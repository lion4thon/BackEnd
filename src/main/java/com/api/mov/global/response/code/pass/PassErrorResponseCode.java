package com.api.mov.global.response.code.pass;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PassErrorResponseCode implements BaseResponseCode {
    PASS_NOT_FOUND_404("PASS_NOT_FOUND_404",404,"해당 패키지를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
