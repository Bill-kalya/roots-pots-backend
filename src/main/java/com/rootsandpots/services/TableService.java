package com.rootsandpots.services;

import com.rootsandpots.models.DiningTable;
import com.rootsandpots.repositories.TableRepository;
import com.rootsandpots.repositories.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final RestaurantRepository restaurantRepository;

    public List<DiningTable> getTablesByRestaurant(UUID restaurantId) {
        restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return tableRepository.findByRestaurantIdOrderByTableNumber(restaurantId);
    }

    public DiningTable getTableById(UUID id) {
        return tableRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }

    @Transactional
    public List<DiningTable> createTables(List<DiningTable> tables) {
        return tables.stream()
            .map(table -> {
                restaurantRepository.findById(table.getRestaurant().getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));
                return tableRepository.save(table);
            })
            .toList();
    }

    @Transactional
    public DiningTable updateTable(UUID id, DiningTable tableDetails) {
        DiningTable table = getTableById(id);

        table.setTableNumber(tableDetails.getTableNumber());
        table.setSeats(tableDetails.getSeats());
        table.setX(tableDetails.getX());
        table.setY(tableDetails.getY());
        table.setZone(tableDetails.getZone());
        table.setActive(tableDetails.getActive());

        return tableRepository.save(table);
    }

    @Transactional
    public DiningTable updateTableAvailability(UUID id, boolean available) {
        DiningTable table = getTableById(id);
        return tableRepository.save(table);
    }

    @Transactional
    public void deleteTable(UUID id) {
        DiningTable table = getTableById(id);
        table.setActive(false);
        tableRepository.save(table);
    }

    public List<DiningTable> getActiveTablesByRestaurant(UUID restaurantId) {
        return tableRepository.findByRestaurantIdAndActiveTrue(restaurantId);
    }

    public int countActiveTablesByRestaurant(UUID restaurantId) {
        return tableRepository.countActiveTablesByRestaurant(restaurantId);
    }
}
