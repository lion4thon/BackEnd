package com.api.mov.global.response.code.facility;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FacilityErrorResponseCode implements BaseResponseCode {
    NOT_FOUND_FACILITY("NOT_FOUND_FACILITY_404",404, "해당 업장을 찾을 수 없습니다.");


    private final String code;
    private final int httpStatus;
    private final String message;
}
