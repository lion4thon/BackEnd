package com.api.mov.domain.cart.web.controller;

import com.api.mov.domain.cart.service.CartService;
import com.api.mov.domain.cart.web.dto.CartSummaryRes;
import com.api.mov.global.jwt.UserPrincipal;
import com.api.mov.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CartController {
    private final CartService cartService;

    @GetMapping("/summary")
    public ResponseEntity<SuccessResponse<?>> getCartSummary(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        CartSummaryRes cartSummaryRes = cartService.getCartSummary(userPrincipal.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.okCustom(cartSummaryRes,"장바구니 요약 조회에 성공했습니다."));
    }
}
