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

        //passIds.size() => 사용자가 결제하겠다고 보내준 패키지 id의 개수
        //서버에 있는 사용자의 패키지 중 해당하는 개수
        // 이 개수가 다르다면 문제가 있다는 뜻 -> 즉시 프로세스 중단시키고 잘못된 요청이라는 것을 반환하는 방어 코드
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
