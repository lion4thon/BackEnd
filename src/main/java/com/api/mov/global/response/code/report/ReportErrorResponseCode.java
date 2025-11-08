package com.api.mov.global.response.code.report;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportErrorResponseCode implements BaseResponseCode {
    NOT_FOUND_REPORT("NOT_FOUND_REPORT_404", 404, "해당 리포트를 찾을 수 없습니다."),
    NOT_FOUND_PASS("NOT_FOUND_PASS_404", 404, "해당 패키지를 찾을 수 없습니다."),
    NO_PERMISSION("NO_PERMISSION_403", 403, "리포트를 조회할 권한이 없습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
