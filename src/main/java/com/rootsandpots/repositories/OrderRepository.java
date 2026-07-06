package com.rootsandpots.repositories;

import com.rootsandpots.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.createdAt >= :start AND o.createdAt < :end")
    List<Order> findByCreatedAtBetween(
        @Param("start") OffsetDateTime start,
        @Param("end") OffsetDateTime end
    );

    List<Order> findByTableNumber(Integer tableNumber);
}
