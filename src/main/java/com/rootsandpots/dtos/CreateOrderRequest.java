package com.rootsandpots.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateOrderRequest {

    @NotNull(message = "Reservation ID is required")
    private UUID reservationId;

    @NotEmpty(message = "Order items are required")
    private List<OrderItemRequest> items;
}
