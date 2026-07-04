package com.rootsandpots.services;

import com.rootsandpots.dtos.ReservationRequest;
import com.rootsandpots.dtos.ReservationResponse;
import com.rootsandpots.models.DiningTable;
import com.rootsandpots.models.Reservation;
import com.rootsandpots.repositories.ReservationRepository;
import com.rootsandpots.repositories.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final WebSocketService webSocketService;

    @Transactional
    public ReservationResponse reserveTable(ReservationRequest request) {
        DiningTable table = tableRepository.findByIdForUpdate(request.getTableId())
            .orElseThrow(() -> new RuntimeException("Table not found"));

        List<Reservation> existingReservations = reservationRepository
            .findActiveReservationsForTable(table, request.getDate(), request.getTime());

        if (!existingReservations.isEmpty()) {
            throw new RuntimeException("Table already reserved for this time");
        }

        Reservation reservation = new Reservation();
        reservation.setTable(table);
        reservation.setCustomerName(request.getCustomerName());
        reservation.setCustomerPhone(request.getCustomerPhone());
        reservation.setReservationDate(request.getDate());
        reservation.setReservationTime(request.getTime());
        reservation.setStatus("CONFIRMED");

        Reservation saved = reservationRepository.save(reservation);

        webSocketService.broadcastTableUpdate(table.getId(), false);

        return mapToResponse(saved);
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setTableId(reservation.getTable().getId());
        response.setTableNumber(reservation.getTable().getTableNumber());
        response.setCustomerName(reservation.getCustomerName());
        response.setDate(reservation.getReservationDate());
        response.setTime(reservation.getReservationTime());
        response.setStatus(reservation.getStatus());
        return response;
    }
}
