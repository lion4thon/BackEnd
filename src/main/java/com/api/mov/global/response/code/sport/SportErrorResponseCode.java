package com.api.mov.global.response.code.sport;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SportErrorResponseCode implements BaseResponseCode {

    NOT_FOUND_SPORT("NOT_FOUND_SPORT_404", 404, "해당하는 운동종목을 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
