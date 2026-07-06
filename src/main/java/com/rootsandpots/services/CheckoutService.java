package com.rootsandpots.services;

import com.rootsandpots.dtos.CheckoutOrderItemRequest;
import com.rootsandpots.dtos.CheckoutRequest;
import com.rootsandpots.dtos.CheckoutResponse;
import com.rootsandpots.models.DiningTable;
import com.rootsandpots.models.Order;
import com.rootsandpots.models.OrderItem;
import com.rootsandpots.models.OrderStatus;
import com.rootsandpots.models.Payment;
import com.rootsandpots.models.Reservation;
import com.rootsandpots.repositories.OrderRepository;
import com.rootsandpots.repositories.PaymentRepository;
import com.rootsandpots.repositories.ReservationRepository;
import com.rootsandpots.repositories.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final WebSocketService webSocketService;

    @Transactional
    public CheckoutResponse createCheckout(CheckoutRequest request) {
        DiningTable table = tableRepository.findById(request.getTableId())
            .orElseThrow(() -> new IllegalArgumentException("Table not found"));

        List<Reservation> existingReservations = reservationRepository
            .findActiveReservationsForTable(table, request.getDate(), request.getTime());

        if (!existingReservations.isEmpty()) {
            throw new IllegalStateException("Table already reserved for this time");
        }

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setCustomerEmail(request.getCustomerEmail());
        reservation.setReservationDate(request.getDate());
        reservation.setReservationTime(request.getTime());
        reservation.setGuests(request.getGuests());
        reservation.setSpecialRequests(request.getSpecialRequests());
        reservation.setStatus("CONFIRMED");

        Reservation savedReservation = reservationRepository.save(reservation);

        Order order = new Order();
        order.setReservation(savedReservation);
        order.setTableNumber(table.getTableNumber());
        order.setStatus(OrderStatus.PAID);
        order.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        order.setUpdatedAt(order.getCreatedAt());

        BigDecimal foodTotal = calculateFoodTotal(request.getItems());
        order.setTotal(foodTotal.add(request.getReservationDeposit() == null ? BigDecimal.ZERO : request.getReservationDeposit()));

        List<OrderItem> items = request.getItems() == null ? List.of() : request.getItems().stream().map(item -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setName(item.getName());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getPrice());
            orderItem.setSpecialRequests(item.getSpecialRequests());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());
        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        Payment payment = new Payment();
        payment.setReservation(savedReservation);
        payment.setAmount(order.getTotal());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionReference(generateReference(savedReservation.getId()));
        payment.setStatus("PAID");
        Payment savedPayment = paymentRepository.save(payment);

        webSocketService.broadcastTableUpdate(table.getId(), false);

        CheckoutResponse response = new CheckoutResponse();
        response.setReservationId(savedReservation.getId());
        response.setOrderId(savedOrder.getId());
        response.setPaymentId(savedPayment.getId());
        response.setTableNumber(table.getTableNumber());
        response.setCustomerName(savedReservation.getCustomerName());
        response.setCustomerPhone(savedReservation.getCustomerPhone());
        response.setCustomerEmail(savedReservation.getCustomerEmail());
        response.setDate(savedReservation.getReservationDate());
        response.setTime(savedReservation.getReservationTime());
        response.setGuests(savedReservation.getGuests());
        response.setSpecialRequests(savedReservation.getSpecialRequests());
        response.setReservationStatus(savedReservation.getStatus());
        response.setOrderStatus(savedOrder.getStatus());
        response.setPaymentStatus(savedPayment.getStatus());
        response.setPaymentMethod(savedPayment.getPaymentMethod());
        response.setPaymentReference(savedPayment.getTransactionReference());
        response.setTotalAmount(savedOrder.getTotal());
        response.setFoodTotal(foodTotal);
        response.setReservationDeposit(request.getReservationDeposit() == null ? BigDecimal.ZERO : request.getReservationDeposit());
        response.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        return response;
    }

    private BigDecimal calculateFoodTotal(List<CheckoutOrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return items.stream()
            .filter(item -> item.getPrice() != null && item.getQuantity() != null)
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateReference(UUID reservationId) {
        return "CHK-" + reservationId.toString().substring(0, 8).toUpperCase();
    }
}
