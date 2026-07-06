package com.rootsandpots.dtos;

import com.rootsandpots.models.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class CheckoutResponse {
    private UUID reservationId;
    private UUID orderId;
    private UUID paymentId;
    private Integer tableNumber;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private LocalDate date;
    private LocalTime time;
    private Integer guests;
    private String specialRequests;
    private String reservationStatus;
    private OrderStatus orderStatus;
    private String paymentStatus;
    private String paymentMethod;
    private String paymentReference;
    private BigDecimal totalAmount;
    private BigDecimal foodTotal;
    private BigDecimal reservationDeposit;
    private OffsetDateTime createdAt;
}
