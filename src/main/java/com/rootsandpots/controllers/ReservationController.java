package com.rootsandpots.controllers;

import com.rootsandpots.dtos.ReservationRequest;
import com.rootsandpots.dtos.ReservationResponse;
import com.rootsandpots.services.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
    
    private final ReservationService reservationService;
    
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
        @Valid @RequestBody ReservationRequest request
    ) {
        ReservationResponse response = reservationService.reserveTable(request);
        return ResponseEntity.ok(response);
    }
}