package com.rootsandpots.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemResponse {
    private UUID id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private String specialRequests;
}
