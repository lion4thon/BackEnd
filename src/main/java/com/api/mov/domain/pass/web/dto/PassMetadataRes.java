package com.api.mov.domain.pass.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * AI 추천 시스템용 패키지 메타데이터 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassMetadataRes {

    private Long passId;
    private String name;
    private Integer price;
    private String intensity;      // LOW, MID, HIGH
    private String purposeTag;     // DIET, REHAB, FITNESS, STRESS_RELIEF, EXPLORE

    public static PassMetadataRes from(com.api.mov.domain.pass.entity.Pass pass) {
        return PassMetadataRes.builder()
                .passId(pass.getId())
                .name(pass.getName())
                .price(pass.getPrice())
                .intensity(pass.getIntensity())
                .purposeTag(pass.getPurposeTag())
                .build();
    }
}
