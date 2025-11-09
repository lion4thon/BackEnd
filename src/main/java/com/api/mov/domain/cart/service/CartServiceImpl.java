package com.api.mov.domain.cart.service;

import com.api.mov.domain.cart.web.dto.CartSummaryRes;
import com.api.mov.domain.pass.entity.Pass;
import com.api.mov.domain.reservation.entity.Reservation;
import com.api.mov.domain.reservation.entity.ReservationStatus;
import com.api.mov.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ReservationRepository reservationRepository;

    @Override
    @Transactional(readOnly = true)
    public CartSummaryRes getCartSummary(Long userId) {

        //사용자의 장바구니 예약 목록을 가져온다 -> pass 정보와 함께 가져온다.
        List<Reservation> reservationList = reservationRepository.findAllByUserIdAndStatusWithPass(userId, ReservationStatus.IN_CART);

        //예약 목록에서 중복되지 않는 Pass들을 추출한다.
        Set<Pass> passList = reservationList.stream()
                .map(Reservation::getPass)
                .collect(Collectors.toSet());

        List<String> passNameList = passList.stream()
                .map(Pass::getName)
                .toList();

        int totalPrice = passList.stream()
                .mapToInt(Pass::getPrice)
                .sum();

        return new CartSummaryRes(
                passNameList,
                totalPrice
        );
    }
}
