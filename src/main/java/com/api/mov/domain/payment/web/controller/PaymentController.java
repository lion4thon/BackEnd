package com.api.mov.domain.payment.web.controller;

import com.api.mov.domain.payment.service.PaymentService;
import com.api.mov.domain.payment.web.dto.PaymentReq;
import com.api.mov.domain.payment.web.dto.PaymentRes;
import com.api.mov.global.jwt.UserPrincipal;
import com.api.mov.global.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/complete")
    public ResponseEntity<SuccessResponse<?>> completePayment(
            @RequestBody PaymentReq paymentReq,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ){
        PaymentRes paymentRes = paymentService.completePayment(paymentReq, userPrincipal.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SuccessResponse.okCustom(paymentRes,"결제가 완료되었습니다."));
    }
}
