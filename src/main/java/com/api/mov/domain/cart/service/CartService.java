package com.api.mov.domain.cart.service;

import com.api.mov.domain.cart.web.dto.CartSummaryRes;

public interface CartService {
    CartSummaryRes getCartSummary(Long userId);
}
