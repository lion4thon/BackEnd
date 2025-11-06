package com.api.mov.global.response.code.reservation;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationErrorResponseCode implements BaseResponseCode {
    RESERVATION_CONFLICT_409("RESERVATION_CONFLICT_409",409, "이미 예약이 존재합니다.");


    private final String code;
    private final int httpStatus;
    private final String message;
}
