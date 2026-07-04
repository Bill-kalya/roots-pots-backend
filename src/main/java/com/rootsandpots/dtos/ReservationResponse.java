package com.rootsandpots.dtos;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class ReservationResponse {
    private UUID id;
    private UUID tableId;
    private Integer tableNumber;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private LocalDate date;
    private LocalTime time;
    private Integer guests;
    private String status;
    private String specialRequests;
    private String createdAt;
}
