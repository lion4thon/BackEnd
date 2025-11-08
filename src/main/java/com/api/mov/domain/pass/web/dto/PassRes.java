package com.api.mov.domain.pass.web.dto;

import java.util.List;

// 패키지 조회를 위한 패키지 정보 response
public record PassRes(
        Long passId,
        String passName,
        int passPrice,
        String passDescription,
        List<FacilityInPassRes> facilities
) {
}
