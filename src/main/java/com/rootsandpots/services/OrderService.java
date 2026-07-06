package com.rootsandpots.services;

import com.rootsandpots.dtos.CreateOrderRequest;
import com.rootsandpots.dtos.OrderItemRequest;
import com.rootsandpots.dtos.OrderItemResponse;
import com.rootsandpots.dtos.OrderResponse;
import com.rootsandpots.models.Order;
import com.rootsandpots.models.OrderItem;
import com.rootsandpots.models.OrderStatus;
import com.rootsandpots.models.Reservation;
import com.rootsandpots.repositories.OrderRepository;
import com.rootsandpots.repositories.ReservationRepository;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
            .orElseThrow(() -> new IllegalStateException("Reservation not found"));

        Order order = new Order();
        order.setReservation(reservation);
        order.setTableNumber(reservation.getTable().getTableNumber());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = request.getItems().stream()
            .map(itemRequest -> {
                OrderItem item = new OrderItem();
                item.setName(itemRequest.getName());
                item.setQuantity(itemRequest.getQuantity());
                item.setPrice(itemRequest.getPrice());
                item.setSpecialRequests(itemRequest.getSpecialRequests());
                item.setOrder(order);
                return item;
            })
            .collect(Collectors.toList());

        order.setItems(orderItems);
        order.setTotal(calculateTotal(orderItems));
        order.setCreatedAt(OffsetDateTime.now(ZoneOffset.UTC));
        order.setUpdatedAt(order.getCreatedAt());

        Order saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    public OrderResponse getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
            .map(this::mapToResponse)
            .orElseThrow(() -> new IllegalStateException("Order not found"));
    }

    public List<OrderResponse> getOrdersByDate(OffsetDateTime date) {
        OffsetDateTime startOfDay = date.toLocalDate().atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime endOfDay = startOfDay.plusDays(1);
        return orderRepository.findByCreatedAtBetween(startOfDay, endOfDay).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public List<OrderResponse> getOrdersByTable(Integer tableNumber) {
        return orderRepository.findByTableNumber(tableNumber).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalStateException("Order not found"));

        order.setStatus(status);
        order.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));

        return mapToResponse(orderRepository.save(order));
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setReservationId(order.getReservation().getId());
        response.setTableNumber(order.getTableNumber());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        response.setItems(order.getItems().stream().map(this::mapItem).collect(Collectors.toList()));
        return response;
    }

    private OrderItemResponse mapItem(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setSpecialRequests(item.getSpecialRequests());
        return response;
    }
}
