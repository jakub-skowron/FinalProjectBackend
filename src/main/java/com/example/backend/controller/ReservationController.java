package com.example.backend.controller;

import com.example.backend.model.Reservation;
import com.example.backend.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    ReservationService reservationService;

    @GetMapping("")
    public List<Reservation> getAllReservations() {
        return reservationService.getReservations();
    }

    @GetMapping("/{id}")
    public Reservation getReservationById(@PathVariable long id) {
        return reservationService.getReservationById(id);
    }

    @PostMapping("")
    public void addOneReservation(@RequestBody Reservation reservation) {
        reservationService.addReservation(reservation);
    }

    @DeleteMapping("/{id}")
    public void removeOneReservation(@PathVariable Long id) {
        reservationService.removeReservationById(id);
    }

    @PutMapping("/{id}")
    public void updateOneReservation(@PathVariable Long id, @RequestBody Reservation reservation) {
        reservationService.updateReservationIdentifierById(id, reservation);
    }
}
