package com.example.backend.service;

import com.example.backend.exceptions.ObjectAlreadyExistsException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.model.Reservation;
import com.example.backend.model.Room;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.RoomRepository;
import jakarta.annotation.Resource;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservationServiceTest {
    private Validator validator;
    private Reservation reservation;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    RoomService roomService;

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
    void ShouldNotReturnViolations() {
        Set<ConstraintViolation<Reservation>> violations = validator.validate(reservation);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(0).isEqualTo(violations.size());
        softly.assertAll();
    }

    @ParameterizedTest
    @CsvSource({"A, size must be between 2 and 20", "This name is too long, size must be between 2 and 20"})
    public void ifIdentifierIsInvalidShouldReturnViolation(String identifier, String expectedErrorMessage) {
        reservation.setIdentifier(identifier);

        Set<ConstraintViolation<Reservation>> violations = validator.validate(reservation);
        ConstraintViolation<Reservation> violation = violations.iterator().next();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(1).isEqualTo(violations.size());
        softly.assertThat(expectedErrorMessage).isEqualTo(violation.getMessage());
        softly.assertThat("identifier").isEqualTo(violation.getPropertyPath().toString());
        softly.assertAll();
    }

    @Test
    void getReservationsShouldPass() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Reservation> result = reservationService.getReservations();

        assertEquals(1, result.size());
    }

    @Test
    public void getReservationByIdShouldPass() {
        when(reservationRepository.existsById(reservation.getId())).thenReturn(true);
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.getReservationById(reservation.getId());
        assertEquals(reservation, result);
    }

    @Test
    void addReservationShouldPass() {
        Map<Room.PlaceType, Integer> places = new HashMap<>();
        places.put(Room.PlaceType.SITTING, 3);
        places.put(Room.PlaceType.STANDING, 4);
        Room room = new Room.Builder()
                .withId(1)
                .withName("Valid Name")
                .withIdentifier("Valid Identifier")
                .withLevel(1)
                .withAvailability(true)
                .withPlaces(places)
                .build();
        when(roomRepository.existsById(reservation.getRoomId())).thenReturn(true);
        when(reservationRepository.existsById(reservation.getId())).thenReturn(false);
        when(reservationRepository.existsByIdentifier(reservation.getIdentifier())).thenReturn(false);
        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));
        when(reservationRepository.findByRoom_IdAndStartReservationDateTimeLessThanEqualAndEndReservationDateTimeGreaterThanEqual(
                reservation.getRoomId(),
                reservation.getEndReservationDateTime(),
                reservation.getStartReservationDateTime())).thenReturn(Optional.empty());

        reservationService.addReservation(reservation);

        verify(reservationRepository).save(reservation);
    }

    @Test
    public void addReservationWithRoomInvalidIdShouldThrowException() {
        when(roomRepository.existsById(reservation.getRoomId())).thenReturn(false);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            reservationService.addReservation(reservation);
        });
    }

    @Test
    public void addReservationWithExistedIdShouldThrowException() {
        when(roomRepository.existsById(reservation.getRoomId())).thenReturn(true);
        when(reservationRepository.existsById(reservation.getId())).thenReturn(true);

        Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
            reservationService.addReservation(reservation);
        });
    }

    @Test
    public void addReservationWithExistedIdentifierShouldThrowException() {
        when(roomRepository.existsById(reservation.getRoomId())).thenReturn(true);
        when(reservationRepository.existsById(reservation.getId())).thenReturn(false);
        when(reservationRepository.existsById(reservation.getId())).thenReturn(true);

        Assertions.assertThrows(ObjectAlreadyExistsException.class, () -> {
            reservationService.addReservation(reservation);
        });
    }

    @Test
    public void addReservationWithInvalidRoomAvailabilityShouldThrowException() {
        Map<Room.PlaceType, Integer> places = new HashMap<>();
        places.put(Room.PlaceType.SITTING, 3);
        places.put(Room.PlaceType.STANDING, 4);
        Room room = new Room.Builder()
                .withId(1)
                .withName("Valid Name")
                .withIdentifier("Valid Identifier")
                .withLevel(1)
                .withAvailability(false)
                .withPlaces(places)
                .build();
        when(roomRepository.existsById(reservation.getRoomId())).thenReturn(true);
        when(reservationRepository.existsById(reservation.getId())).thenReturn(false);
        when(reservationRepository.existsById(reservation.getId())).thenReturn(false);
        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            reservationService.addReservation(reservation);
        });
    }

    @ParameterizedTest
    @CsvSource({"2, 1, 2053, The set date is invalid! Start Date is after End Date",
            "1, 1, 2053, The set date is invalid! Start Date is equal End Date",
            "1, 2, 1995, The selected date is invalid! It cannot be set to a past date"})
    public void addReservationWhenStartDateIsAfterEndDateShouldThrowException(int startTime,
                                                                              int endTime,
                                                                              int year,
                                                                              String message) {
        Map<Room.PlaceType, Integer> places = new HashMap<>();
        places.put(Room.PlaceType.SITTING, 3);
        places.put(Room.PlaceType.STANDING, 4);
        Room room = new Room.Builder()
                .withId(1)
                .withName("Valid Name")
                .withIdentifier("Valid Identifier")
                .withLevel(1)
                .withAvailability(true)
                .withPlaces(places)
                .build();

        reservation.setStartReservationDateTime(LocalDateTime.of(year, 12, 18, startTime, 30));
        reservation.setEndReservationDateTime(LocalDateTime.of(year, 12, 18, endTime, 30));

        when(roomRepository.existsById(reservation.getRoomId())).thenReturn(true);
        when(reservationRepository.existsById(reservation.getId())).thenReturn(false);
        when(reservationRepository.existsByIdentifier(reservation.getIdentifier())).thenReturn(false);
        when(roomRepository.findById(reservation.getRoomId())).thenReturn(Optional.of(room));

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            reservationService.addReservation(reservation);
        });
        System.out.println(exception.getMessage());
        assertEquals(message, exception.getMessage());
    }

    @Test
    void removeReservationById() {
        when(reservationRepository.existsById(reservation.getId())).thenReturn(true);
        when(reservationRepository.findById(reservation.getId())).thenReturn(Optional.of(reservation));

        reservationService.removeReservationById(reservation.getId());
        verify(reservationRepository).deleteById(reservation.getId());
    }

    @Test
    void checkIfRoomIsNotAlreadyBookedInThisDate() {
        when(reservationRepository.findByRoom_IdAndStartReservationDateTimeLessThanEqualAndEndReservationDateTimeGreaterThanEqual(
                reservation.getRoomId(),
                reservation.getEndReservationDateTime(),
                reservation.getStartReservationDateTime())).thenReturn(Optional.of(reservation));

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            reservationService.addReservation(reservation);
        });
    }
}