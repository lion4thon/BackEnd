package com.api.mov.domain.reservation.service;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.facility.repository.FacilityRepository;
import com.api.mov.domain.reservation.entity.Reservation;
import com.api.mov.domain.reservation.entity.ReservationStatus;
import com.api.mov.domain.reservation.repository.ReservationRepository;
import com.api.mov.domain.reservation.web.dto.ReservationCreateReq;
import com.api.mov.domain.user.entity.User;
import com.api.mov.domain.user.repository.UserRepository;
import com.api.mov.global.exception.CustomException;
import com.api.mov.global.response.code.facility.FacilityErrorResponseCode;
import com.api.mov.global.response.code.reservation.ReservationErrorResponseCode;
import com.api.mov.global.response.code.user.UserErrorResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;


    @Override
    @Transactional
    public void createReservation(Long userId, ReservationCreateReq reservationCreateReq) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new CustomException(UserErrorResponseCode.USER_NOT_FOUND_404));

        Facility facility = facilityRepository.findById(reservationCreateReq.getFacilityId())
                .orElseThrow(()->new CustomException(FacilityErrorResponseCode.NOT_FOUND_FACILITY_404));

        //더블 부킹 체크
        List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(
                facility,
                reservationCreateReq.getStartTime(),
                reservationCreateReq.getEndTime()
        );

        //겹치는 예약 리스트가 존재한다면 이미 예약이 있다는 것
        if(!overlappingReservations.isEmpty()) {
            throw new CustomException(ReservationErrorResponseCode.RESERVATION_CONFLICT_409);
        }

        Reservation reservation = Reservation.builder()
                .user(user)
                .facility(facility)
                .startTime(reservationCreateReq.getStartTime())
                .endTime(reservationCreateReq.getEndTime())
                .status(ReservationStatus.IN_CART)
                .build();

        reservationRepository.save(reservation);
    }
}
