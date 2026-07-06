package com.rootsandpots.services;

import com.rootsandpots.models.Payment;
import com.rootsandpots.models.Reservation;
import com.rootsandpots.repositories.PaymentRepository;
import com.rootsandpots.repositories.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldCreateAndVerifyPayment() {
        UUID reservationId = UUID.randomUUID();
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            payment.setId(UUID.randomUUID());
            return payment;
        });
        when(paymentRepository.findById(any(UUID.class))).thenAnswer(invocation -> {
            Payment payment = new Payment();
            payment.setId(invocation.getArgument(0));
            payment.setStatus("PAID");
            payment.setTransactionReference("TX-001");
            return Optional.of(payment);
        });

        Payment created = paymentService.createPayment(reservationId, "M-PESA", new BigDecimal("1500.00"), "TX-001");

        assertNotNull(created);
        assertEquals("PAID", created.getStatus());
        assertEquals("TX-001", created.getTransactionReference());

        Payment verified = paymentService.verifyPayment(created.getId());
        assertEquals("PAID", verified.getStatus());
    }
}
