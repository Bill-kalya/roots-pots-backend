package com.rootsandpots.controllers;

import com.rootsandpots.models.Reservation;
import com.rootsandpots.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminController {

    private final ReservationRepository reservationRepository;

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getReservations(
            @RequestParam(required = false) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(reservationRepository.findReservationsForDate(date));
        }
        return ResponseEntity.ok(reservationRepository.findAll());
    }

    @PatchMapping("/reservations/{id}")
    public ResponseEntity<Reservation> updateReservationStatus(
            @PathVariable UUID id,
            @RequestBody Map<String, String> request) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        String status = request.get("status");
        if (status != null) {
            reservation.setStatus(status);
        }

        return ResponseEntity.ok(reservationRepository.save(reservation));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", reservationRepository.count());
        stats.put("today", reservationRepository.countConfirmedReservationsForDate(LocalDate.now()));
        return ResponseEntity.ok(stats);
    }
}
