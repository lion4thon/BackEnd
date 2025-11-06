package com.api.mov.domain.reservation.web.controller;

import com.api.mov.domain.reservation.service.ReservationService;
import com.api.mov.domain.reservation.web.dto.ReservationCreateReq;
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
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservations")
    public ResponseEntity<SuccessResponse<?>> createReservation(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                @RequestBody ReservationCreateReq reservationCreateReq) {
        reservationService.createReservation(userPrincipal.getId(), reservationCreateReq);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.success("예약에 성공하였습니다."));

    }
}
