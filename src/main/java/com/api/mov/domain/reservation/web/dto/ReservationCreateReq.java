package com.api.mov.domain.reservation.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReservationCreateReq {

    private Long facilityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
