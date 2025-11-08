package com.api.mov.domain.pass.service;

import com.api.mov.domain.pass.web.dto.PassCreateReq;
import com.api.mov.domain.pass.web.dto.PassRes;

import java.util.List;

public interface PassService {
    void createPass(PassCreateReq passCreateReq, Long userId);

    // 패키지 상세 정보 조회
    PassRes getPassDetail(Long passId);
}
