package com.rootsandpots.services;

import com.rootsandpots.dtos.CheckoutRequest;
import com.rootsandpots.dtos.CheckoutResponse;
import com.rootsandpots.models.DiningTable;
import com.rootsandpots.models.Order;
import com.rootsandpots.models.OrderStatus;
import com.rootsandpots.models.Payment;
import com.rootsandpots.models.Reservation;
import com.rootsandpots.models.Restaurant;
import com.rootsandpots.repositories.OrderRepository;
import com.rootsandpots.repositories.PaymentRepository;
import com.rootsandpots.repositories.ReservationRepository;
import com.rootsandpots.repositories.TableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TableRepository tableRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private WebSocketService webSocketService;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void shouldCreateReservationOrderAndPaymentTogether() {
        CheckoutRequest request = new CheckoutRequest();
        request.setTableId(UUID.randomUUID());
        request.setCustomerName("Jane Doe");
        request.setCustomerPhone("254700000000");
        request.setCustomerEmail("jane@example.com");
        request.setDate(LocalDate.now());
        request.setTime(LocalTime.of(19, 0));
        request.setGuests(4);
        request.setSpecialRequests("Window seat");
        request.setPaymentMethod("M-PESA");
        request.setReservationDeposit(new BigDecimal("500.00"));
        request.setItems(List.of(new com.rootsandpots.dtos.CheckoutOrderItemRequest("Burger", 2, new BigDecimal("800.00"), "No onions")));

        DiningTable table = new DiningTable();
        table.setId(request.getTableId());
        table.setTableNumber(7);
        table.setRestaurant(new Restaurant());
        table.getRestaurant().setId(UUID.randomUUID());

        when(tableRepository.findById(request.getTableId())).thenReturn(Optional.of(table));
        when(reservationRepository.findActiveReservationsForTable(any(), any(), any())).thenReturn(List.of());
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation reservation = invocation.getArgument(0);
            reservation.setId(UUID.randomUUID());
            return reservation;
        });
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(UUID.randomUUID());
            return order;
        });
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> {
            Payment payment = invocation.getArgument(0);
            payment.setId(UUID.randomUUID());
            return payment;
        });

        CheckoutResponse response = checkoutService.createCheckout(request);

        assertNotNull(response);
        assertNotNull(response.getReservationId());
        assertNotNull(response.getOrderId());
        assertNotNull(response.getPaymentId());
        assertEquals("CONFIRMED", response.getReservationStatus());
        assertEquals(OrderStatus.PAID, response.getOrderStatus());
    }
}
