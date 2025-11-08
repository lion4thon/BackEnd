package com.api.mov.global.response.code.report;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportErrorResponseCode implements BaseResponseCode {
    NOT_FOUND_REPORT("NOT_FOUND_REPORT_404", 404, "해당 리포트를 찾을 수 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
