package com.rootsandpots.services;

import com.rootsandpots.models.Payment;
import com.rootsandpots.models.Reservation;
import com.rootsandpots.repositories.PaymentRepository;
import com.rootsandpots.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Payment createPayment(UUID reservationId, String paymentMethod, BigDecimal amount, String transactionReference) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));

        Payment payment = new Payment();
        payment.setReservation(reservation);
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setTransactionReference(transactionReference);
        payment.setStatus("PAID");
        payment.setPaidAt(OffsetDateTime.now());
        return paymentRepository.save(payment);
    }

    @Transactional(readOnly = true)
    public Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }

    @Transactional
    public Payment verifyPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        payment.setStatus("PAID");
        payment.setPaidAt(OffsetDateTime.now());
        return paymentRepository.save(payment);
    }
}
