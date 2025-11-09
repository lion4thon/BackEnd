package com.api.mov.domain.cart.web.dto;

import java.util.List;

public record CartSummaryRes(
        List<String> passName,
        int totalPrice
) {
}
