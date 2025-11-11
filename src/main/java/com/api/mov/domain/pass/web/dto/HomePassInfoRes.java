package com.api.mov.domain.pass.web.dto;

import java.util.List;

public record HomePassInfoRes(
        Long passId,
        String passName,
        String passDescription,
        int passPrice
) {
}
