package com.api.mov.domain.pass.web.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PassCreateReq {
    private List<Long> facilityIdList;
    private int passPrice;
    private String passName;
    private String passDescription;
}
