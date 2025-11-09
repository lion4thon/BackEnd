package com.api.mov.domain.payment.service;

import com.api.mov.domain.payment.web.dto.PaymentReq;
import com.api.mov.domain.payment.web.dto.PaymentRes;

public interface PaymentService {
    PaymentRes completePayment(PaymentReq paymentReq, Long userId);
}
