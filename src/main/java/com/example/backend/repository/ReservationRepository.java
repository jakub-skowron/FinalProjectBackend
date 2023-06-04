package com.example.backend.repository;

import com.example.backend.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByIdentifier(String string);

    Optional<Reservation> findByRoom_IdAndStartReservationDateTimeLessThanEqualAndEndReservationDateTimeGreaterThanEqual(
            long roomId, LocalDateTime startDate, LocalDateTime endDate);
}
