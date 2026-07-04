package com.rootsandpots.controllers;

import com.rootsandpots.models.DiningTable;
import com.rootsandpots.services.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TableController {

    private final TableService tableService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<DiningTable>> getTablesByRestaurant(@PathVariable UUID restaurantId) {
        return ResponseEntity.ok(tableService.getTablesByRestaurant(restaurantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiningTable> getTableById(@PathVariable UUID id) {
        return ResponseEntity.ok(tableService.getTableById(id));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<DiningTable>> createTables(@RequestBody List<DiningTable> tables) {
        return ResponseEntity.ok(tableService.createTables(tables));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiningTable> updateTable(
            @PathVariable UUID id,
            @RequestBody DiningTable table) {
        return ResponseEntity.ok(tableService.updateTable(id, table));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<DiningTable> updateTableAvailability(
            @PathVariable UUID id,
            @RequestParam boolean available) {
        return ResponseEntity.ok(tableService.updateTableAvailability(id, available));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable UUID id) {
        tableService.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
