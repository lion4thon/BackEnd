package com.api.mov.domain.pass.web.dto;

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
}
