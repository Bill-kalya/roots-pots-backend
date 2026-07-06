package com.rootsandpots.controllers;

import com.rootsandpots.dtos.CreateOrderRequest;
import com.rootsandpots.dtos.OrderResponse;
import com.rootsandpots.dtos.UpdateOrderStatusRequest;
import com.rootsandpots.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrdersByDate(
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date
    ) {
        if (date == null) {
            date = LocalDate.now(ZoneOffset.UTC);
        }
        OffsetDateTime startOfDay = date.atStartOfDay().atOffset(ZoneOffset.UTC);
        return ResponseEntity.ok(orderService.getOrdersByDate(startOfDay));
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrderStatus(
        @PathVariable UUID orderId,
        @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, request.getStatus()));
    }

    @GetMapping("/table/{tableNumber}")
    public ResponseEntity<List<OrderResponse>> getOrdersByTable(
        @PathVariable Integer tableNumber
    ) {
        return ResponseEntity.ok(orderService.getOrdersByTable(tableNumber));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @Valid @RequestBody CreateOrderRequest request
    ) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }
}
