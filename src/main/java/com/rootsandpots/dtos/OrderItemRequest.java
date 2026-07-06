package com.rootsandpots.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    @NotBlank(message = "Item name is required")
    private String name;

    @NotNull(message = "Item quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Item price is required")
    private BigDecimal price;

    private String specialRequests;
}
