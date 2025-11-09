package com.api.mov.domain.payment.web.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PaymentReq {
    private List<Long> passId;
}
