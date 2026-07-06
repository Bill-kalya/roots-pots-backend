package com.rootsandpots.controllers;

import com.rootsandpots.dtos.CheckoutRequest;
import com.rootsandpots.dtos.CheckoutResponse;
import com.rootsandpots.dtos.ReservationRequest;
import com.rootsandpots.dtos.ReservationResponse;
import com.rootsandpots.services.CheckoutService;
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
    private final CheckoutService checkoutService;
    
    @PostMapping
    public ResponseEntity<ReservationResponse> createReservation(
        @Valid @RequestBody ReservationRequest request
    ) {
        ReservationResponse response = reservationService.reserveTable(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> createCheckout(
        @Valid @RequestBody CheckoutRequest request
    ) {
        return ResponseEntity.ok(checkoutService.createCheckout(request));
    }
}