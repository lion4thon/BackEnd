package com.api.mov.global.response.code.review;

import com.api.mov.global.response.code.BaseResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewErrorResponseCode implements BaseResponseCode {
    PASS_NOT_COMPLETED("REVIEW_403_1", 403, "해당 시설을 이용한 완료된 패키지가 없습니다."),
    REVIEW_ALREADY_EXISTS("REVIEW_409_1", 409, "이미 해당 시설에 대한 후기를 작성했습니다.");

    private final String code;
    private final int httpStatus;
    private final String message;
}
