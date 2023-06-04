package com.example.backend.service;

import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ReservationServiceTest {
    private Validator validator;
    private Reservation reservation;
    @Mock
    ReservationRepository reservationRepository;

    @Resource
    @InjectMocks
    ReservationService reservationService;

    @BeforeEach
    void onInit() {
        MockitoAnnotations.initMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        long id = 1;
        String identifier = "RR22";
        LocalDateTime startReservationDateTime = LocalDateTime.now().plusMinutes(30);
        LocalDateTime endReservationDateTime = LocalDateTime.now().plusHours(1);
        reservation = new Reservation();
        reservation.setId(id);
        reservation.setIdentifier(identifier);
        reservation.setStartReservationDateTime(startReservationDateTime);
        reservation.setEndReservationDateTime(endReservationDateTime);
    }
    @Test
    void getReservationsShouldPass() {

        Set<ConstraintViolation<Reservation>> violations = validator.validate(reservation);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(0).isEqualTo(violations.size());
        softly.assertAll();
    }

    @Test
    public void getReservationByIdShouldPass() {
        when(reservationRepository.existsById(reservation.getId())).thenReturn(true);
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(reservation.getId());
        assertEquals(reservation, result);
    }

    @Test
    void addReservation() {
    }

    @Test
    void removeReservationById() {
    }

    @Test
    void updateReservationNameById() {
    }

    @Test
    void checkIfDateIsValid() {
    }

    @Test
    void checkIfRoomIsNotAlreadyBookedInThisDate() {
    }
}