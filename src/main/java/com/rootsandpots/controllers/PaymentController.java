package com.rootsandpots.controllers;

import com.rootsandpots.models.Payment;
import com.rootsandpots.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Map<String, Object> payload) {
        UUID reservationId = UUID.fromString(payload.get("reservationId").toString());
        String paymentMethod = payload.get("paymentMethod").toString();
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        String transactionReference = payload.get("transactionReference").toString();

        return ResponseEntity.ok(paymentService.createPayment(reservationId, paymentMethod, amount, transactionReference));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.getPayment(paymentId));
    }

    @PostMapping("/{paymentId}/verify")
    public ResponseEntity<Payment> verifyPayment(@PathVariable UUID paymentId) {
        return ResponseEntity.ok(paymentService.verifyPayment(paymentId));
    }
}
