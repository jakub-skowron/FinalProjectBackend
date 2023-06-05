package com.example.backend.repository;

import com.example.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByIdentifier(String string);

    List<Reservation> findAllByRoom_IdAndStartReservationDateTimeLessThanEqualAndEndReservationDateTimeGreaterThanEqual(
            long roomId, LocalDateTime startDate, LocalDateTime endDate);

    List<Reservation> findAllByIdNotAndRoom_IdAndStartReservationDateTimeLessThanEqualAndEndReservationDateTimeGreaterThanEqual(
            long reservationId, long roomId, LocalDateTime endDate, LocalDateTime startDate);

}
