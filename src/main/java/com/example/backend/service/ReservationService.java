package com.example.backend.service;

import com.example.backend.exceptions.*;
import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import com.example.backend.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    Logger LOGGER = LoggerFactory.getLogger(ReservationService.class);
    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

    public List<Reservation> getReservations() {
        LOGGER.info("List all reservations");
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(long id) {
        LOGGER.info("Searching for reservation with id {}", id);
        if (reservationRepository.existsById(id)) {
            LOGGER.info("The reservation with id {} was found", id);
            return reservationRepository.findById(id).get();
        } else {
            LOGGER.debug("The reservation with id {} not found", id);
            throw new ObjectNotFoundException("The Reservation with inserted id doesn't exist");
        }
    }

    public void addReservation(Reservation reservation) {
        LOGGER.info("Reservation adding");
        if (!roomRepository.existsById(reservation.getRoomId())) {
            LOGGER.debug("The room with id {} not found", reservation.getRoomId());
            throw new ObjectNotFoundException("There is no room with inserted id");
        }
        if (reservationRepository.existsById(reservation.getId())) {
            LOGGER.debug("The reservation with id {} already exists", reservation.getId());
            throw new ObjectAlreadyExistsException("The reservation with inserted id already exists");
        }
        if (reservationRepository.existsByIdentifier(reservation.getIdentifier())) {
            LOGGER.debug("The reservation with identifier {} already exists", reservation.getIdentifier());
            throw new ObjectAlreadyExistsException("The reservation with inserted identifier already exists");
        }
        long roomId = reservation.getRoomId();
        LOGGER.info("Reservation date checking");
        checkIfRoomIsAvailable(reservation.getRoomId());
        LOGGER.info("Reservation date checking");
        checkIfDateIsValid(reservation, reservation.getRoomId());
        LOGGER.info("Reservation date checking completed");
        reservation.setRoom(roomService.getRoomById(roomId));
        reservationRepository.save(reservation);
        LOGGER.info("The reservation was saved");
    }

    public void removeReservationById(Long id) {
        LOGGER.info("Reservation deleting");
        if (reservationRepository.existsById(id)) {
            LOGGER.info("The reservation with id {} was found", id);
            reservationRepository.deleteById(id);
            LOGGER.info("The reservation was deleted");
        } else {
            LOGGER.debug("The reservation with id {} not found", id);
            throw new ObjectNotFoundException("The Reservation with inserted id doesn't exist");
        }
    }

    public void updateReservationNameById(Long id, Reservation reservation) {
        LOGGER.info("Reservation updating");
        if (reservationRepository.existsById(id)) {
            LOGGER.info("The reservation with id {} was found", id);
            LOGGER.info("Setting updated fields");
            reservation.setId(id);
            reservationRepository.save(reservation);
            LOGGER.info("The reservation was updated");
        } else {
            LOGGER.debug("The reservation with id {} not found", id);
            throw new ObjectNotFoundException("The Reservation with inserted id doesn't exist");
        }
    }

    private void checkIfRoomIsAvailable(long id) {

    }

    public void checkIfDateIsValid(Reservation reservation, long id) {
        if (reservation.getStartReservationDateTime().isAfter(reservation.getEndReservationDateTime())) {
            LOGGER.debug("The reservation start date is after end date");
            throw new StartDateAfterEndDateException("The set date is invalid! Start Date is after End Date");
        }
        if (reservation.getStartReservationDateTime().isEqual(reservation.getEndReservationDateTime())) {
            LOGGER.debug("The reservation start date equals end date");
            throw new StartDateEqualsEndDateException("The set date is invalid! Start Date is equal End Date");
        }
        if (reservation.getStartReservationDateTime().isBefore(LocalDateTime.now()) ||
                reservation.getEndReservationDateTime().isBefore(LocalDateTime.now())) {
            LOGGER.debug("The reservation dates are set to a past");
            throw new DateInThePastException("The selected date is invalid! It cannot be set to a past date");
        }
        checkIfRoomIsNotAlreadyBookedInThisDate(reservation, id);
    }

    public void checkIfRoomIsNotAlreadyBookedInThisDate(Reservation reservation, long id) {
        boolean condition = reservationRepository
                .findByRoom_IdAndStartReservationDateTimeLessThanEqualAndEndReservationDateTimeGreaterThanEqual(
                id,
                reservation.getEndReservationDateTime(),
                reservation.getStartReservationDateTime()).isPresent();
        if (condition) {
            LOGGER.debug("The room is already booked in this date");
            throw new DateInThePastException("The room is already booked in this date");
        }
    }
}
