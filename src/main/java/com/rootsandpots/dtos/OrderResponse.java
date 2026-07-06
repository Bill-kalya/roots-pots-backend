package com.rootsandpots.dtos;

import com.rootsandpots.models.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResponse {
    private UUID id;
    private UUID reservationId;
    private Integer tableNumber;
    private List<OrderItemResponse> items;
    private OrderStatus status;
    private BigDecimal total;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
