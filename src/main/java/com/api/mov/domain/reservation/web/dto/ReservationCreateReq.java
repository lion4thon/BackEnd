package com.api.mov.domain.reservation.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationCreateReq {

    private Long facilityId;
    private Long passId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
