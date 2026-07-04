package com.rootsandpots.repositories;

import com.rootsandpots.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @Query("SELECT r FROM Restaurant r WHERE r.active = true")
    List<Restaurant> findActiveRestaurants();

    List<Restaurant> findByNameContainingIgnoreCase(String name);
}
