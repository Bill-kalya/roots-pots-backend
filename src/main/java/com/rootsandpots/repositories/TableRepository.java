package com.rootsandpots.repositories;

import com.rootsandpots.models.DiningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TableRepository extends JpaRepository<DiningTable, UUID> {

    List<DiningTable> findByRestaurantIdOrderByTableNumber(UUID restaurantId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM DiningTable t WHERE t.id = :id")
    Optional<DiningTable> findByIdForUpdate(@Param("id") UUID id);

    List<DiningTable> findByRestaurantIdAndActiveTrue(UUID restaurantId);

    @Query("SELECT COUNT(t) FROM DiningTable t WHERE t.restaurant.id = :restaurantId AND t.active = true")
    int countActiveTablesByRestaurant(@Param("restaurantId") UUID restaurantId);
}
