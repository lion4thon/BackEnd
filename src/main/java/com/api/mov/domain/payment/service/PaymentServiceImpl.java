package com.api.mov.domain.payment.service;

import com.api.mov.domain.pass.entity.UserPass;
import com.api.mov.domain.pass.repository.UserPassRepository;
import com.api.mov.domain.payment.web.dto.PaymentReq;
import com.api.mov.domain.payment.web.dto.PaymentRes;
import com.api.mov.domain.reservation.entity.ReservationStatus;
import com.api.mov.domain.reservation.repository.ReservationRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.pass.PassErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ReservationRepository reservationRepository;
    private final UserPassRepository userPassRepository;


    @Override
    @Transactional
    public PaymentRes completePayment(PaymentReq paymentReq, Long userId) {

        List<Long> passIds = paymentReq.getPassId();

        List<UserPass> userPassList = userPassRepository.findAllByUserIdAndPassIdInWithPass(userId, passIds);
        if(userPassList.size() != passIds.size()) {
            throw new CustomException(PassErrorResponseCode.INVALID_PASS_REQUEST_400);
        }

        int totalPrice = userPassList.stream()
                .mapToInt(userPass -> userPass.getPass().getPrice())
                .sum();

        reservationRepository.updateStatusForPasses(passIds, ReservationStatus.IN_CART, ReservationStatus.CONFIRMED);

        return new PaymentRes(passIds, totalPrice);
    }
}
