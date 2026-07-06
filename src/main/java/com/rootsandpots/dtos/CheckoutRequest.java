package com.rootsandpots.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Data
public class CheckoutRequest {

    @NotNull(message = "Table ID is required")
    private UUID tableId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    private String customerPhone;

    private String customerEmail;

    @NotNull(message = "Reservation date is required")
    private LocalDate date;

    @NotNull(message = "Reservation time is required")
    private LocalTime time;

    private Integer guests = 2;

    private String specialRequests;

    private String paymentMethod = "M-PESA";

    private BigDecimal reservationDeposit = BigDecimal.ZERO;

    private List<CheckoutOrderItemRequest> items;
}
