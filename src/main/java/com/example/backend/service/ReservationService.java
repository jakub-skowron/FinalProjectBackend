package com.example.backend.service;

import com.example.backend.exceptions.ObjectAlreadyExistsException;
import com.example.backend.exceptions.ObjectNotFoundException;
import com.example.backend.model.Reservation;
import com.example.backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    public Set<Reservation> getReservations() {
        return new HashSet<>(reservationRepository.findAll());
    }

    public Reservation getReservationById(long id) {
        if (reservationRepository.existsById(id)) {
            return reservationRepository.findById(id).get();
        } else {
            throw new ObjectNotFoundException("The Reservation with inserted id doesn't exist");
        }
    }

    public void addReservation(Reservation reservation) {
        if (!reservationRepository.existsById(reservation.getId()) && !reservationRepository.existsByIdentifier(reservation.getIdentifier())) {
            if (reservation.getStartReservationDateTime().isBefore(reservation.getEndReservationDateTime())) {
                reservationRepository.save(reservation);
            }
            if (reservation.getStartReservationDateTime().isAfter(reservation.getEndReservationDateTime())) {
                throw new IllegalArgumentException("The set date is invalid! Start Date is after End Date!");
            }
            if (reservation.getStartReservationDateTime().isEqual(reservation.getEndReservationDateTime())) {
                throw new IllegalArgumentException("The set date is invalid! Start Date is equal End Date!");
            }
        } else throw new ObjectAlreadyExistsException("The Reservation identifier or id already exists!");
    }

    public void removeReservationById(Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("The Reservation with inserted id doesn't exist");
        }
    }

    public void updateReservationNameById(Long id, Reservation reservation) {
        if (reservationRepository.existsById(id)) {
            reservation.setId(id);
            reservationRepository.save(reservation);
        } else {
            throw new ObjectNotFoundException("The Reservation with inserted id doesn't exist");
        }
    }
}
