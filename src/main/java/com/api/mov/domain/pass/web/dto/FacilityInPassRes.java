package com.api.mov.domain.pass.web.dto;


public record FacilityInPassRes(
        Long facilityId,
        String facilityName,
        String facilityAddress,
        String sportName
){
}
