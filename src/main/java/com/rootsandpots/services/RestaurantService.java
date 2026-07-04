package com.rootsandpots.services;

import com.rootsandpots.models.Restaurant;
import com.rootsandpots.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(UUID id) {
        return restaurantRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
    }

    @Transactional
    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public Restaurant updateRestaurant(UUID id, Restaurant restaurantDetails) {
        Restaurant restaurant = getRestaurantById(id);

        restaurant.setName(restaurantDetails.getName());
        restaurant.setDescription(restaurantDetails.getDescription());
        restaurant.setAddress(restaurantDetails.getAddress());
        restaurant.setLatitude(restaurantDetails.getLatitude());
        restaurant.setLongitude(restaurantDetails.getLongitude());
        restaurant.setHeroImage(restaurantDetails.getHeroImage());
        restaurant.setModelUrl(restaurantDetails.getModelUrl());

        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public void deleteRestaurant(UUID id) {
        Restaurant restaurant = getRestaurantById(id);
        restaurantRepository.delete(restaurant);
    }

    public List<Restaurant> searchRestaurants(String query) {
        return restaurantRepository.findByNameContainingIgnoreCase(query);
    }

    public List<Restaurant> getActiveRestaurants() {
        return restaurantRepository.findActiveRestaurants();
    }
}
