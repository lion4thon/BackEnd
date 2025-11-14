package com.api.mov.domain.pass.web.dto;

import java.util.List;

public record MyPassRes(
        Long passId,
        String passName,
        int passPrice,
        String passDescription,
        List<PassItemInfoRes> passItem,
        String imageUrl
) {
}
