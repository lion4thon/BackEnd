package com.api.mov.domain.pass.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PassCreateReq {
    private List<Long> facilityIdList;
    private int passPrice;
    private String passName;
    private String passDescription;

    @NotBlank(message = "저장 타입을 선택해주세요. (CART or LOCKER)")
    private String storageType;

    private String imageUrl; // 패키지 이미지 URL (선택사항)
}
