package com.api.mov.domain.pass.web.dto;

import java.util.List;

public record PassDetailRes(
        Long passId,
        String passName,
        String passDescription,
        int passPrice,
        List<PassItemInfoRes> passItems
) {
}
