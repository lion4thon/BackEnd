package com.api.mov.domain.reservation.entity;

public enum ReservationStatus {
    /*
        IN_CART => 사용자가 예약을 선택하여 장바구니에 담은 상태
        CONFIRMED => 결제가 완료되어 최종적으로 예약이 확정된 상태
        CANCELLED => 예약을 취소하여 장바구니에서 사라진 상태
     */
    IN_CART, CONFIRMED, CANCELLED

}
