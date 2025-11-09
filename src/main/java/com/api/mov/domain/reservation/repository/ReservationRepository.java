package com.api.mov.domain.reservation.repository;

import com.api.mov.domain.facility.entity.Facility;
import com.api.mov.domain.reservation.entity.Reservation;
import com.api.mov.domain.reservation.entity.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    //취소되지 않은 예약 중 요청된 시간과 겹치는 예약이 있는지 찾아주는 역할
    @Query("SELECT r FROM Reservation r " +
           "WHERE r.facility = :facility " +
           "AND r.status != com.api.mov.domain.reservation.entity.ReservationStatus.CANCELLED " +
           "AND r.startTime < :endTime " +
           "AND r.endTime > :startTime")
    List<Reservation> findOverlappingReservations(@Param("facility") Facility facility,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.pass WHERE r.user.id = :userId AND r.status = :status")
    List<Reservation> findAllByUserIdAndStatusWithPass(@Param("userId") Long userId, @Param("status") ReservationStatus status);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Reservation r SET r.status = :toStatus WHERE r.pass.id IN :passIds AND r.status = :fromStatus")
    int updateStatusForPasses(
            @Param("passIds") List<Long> passIds,
            @Param("fromStatus") ReservationStatus fromStatus,
            @Param("toStatus") ReservationStatus toStatus
    );

}
