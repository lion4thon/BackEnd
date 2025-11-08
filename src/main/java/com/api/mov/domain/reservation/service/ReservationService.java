package com.api.mov.domain.reservation.service;

import com.api.mov.domain.reservation.web.dto.ReservationCreateReq;

public interface ReservationService {

    void createReservation(Long userId, ReservationCreateReq reservationCreateReq);
}
