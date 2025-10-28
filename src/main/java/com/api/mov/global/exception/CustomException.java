package com.api.mov.global.exception;

import com.api.mov.global.response.code.BaseResponseCode;

public class CustomException extends BaseException {

    public CustomException(BaseResponseCode errorCode) {
        super(errorCode);
    }
}