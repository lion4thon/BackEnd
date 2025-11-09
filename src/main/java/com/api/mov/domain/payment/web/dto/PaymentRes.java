package com.api.mov.domain.payment.web.dto;

import java.util.List;

public record PaymentRes(
        List<Long> passId,
        int totalPrice
) {
}
