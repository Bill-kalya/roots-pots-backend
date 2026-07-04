package com.rootsandpots.repositories;

import com.rootsandpots.models.Reservation;
import com.rootsandpots.models.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query("SELECT r FROM Reservation r WHERE r.table = :table AND r.reservationDate = :date AND r.reservationTime = :time AND r.status != 'CANCELLED'")
    List<Reservation> findActiveReservationsForTable(
        @Param("table") DiningTable table,
        @Param("date") LocalDate date,
        @Param("time") LocalTime time
    );

    List<Reservation> findByTableAndReservationDateAndStatusNot(
        DiningTable table,
        LocalDate date,
        String status
    );

    List<Reservation> findByTableIdAndReservationDateAndStatusNot(
        UUID tableId,
        LocalDate date,
        String status
    );

    @Query("SELECT r FROM Reservation r WHERE r.reservationDate = :date AND r.status != 'CANCELLED'")
    List<Reservation> findReservationsForDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.reservationDate = :date AND r.status = 'CONFIRMED'")
    int countConfirmedReservationsForDate(@Param("date") LocalDate date);

    List<Reservation> findByReservationDateBetween(
        LocalDate startDate,
        LocalDate endDate
    );

    @Query("SELECT r FROM Reservation r WHERE r.table.restaurant.id = :restaurantId AND r.reservationDate = :date")
    List<Reservation> findByRestaurantAndDate(
        @Param("restaurantId") UUID restaurantId,
        @Param("date") LocalDate date
    );
}
