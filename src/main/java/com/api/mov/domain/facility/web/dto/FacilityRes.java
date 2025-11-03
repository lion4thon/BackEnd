package com.api.mov.domain.facility.web.dto;

public record FacilityRes(
        Long id,
        String name,
        String address,
        int price
) {
}
