package com.rootsandpots.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@jakarta.persistence.Table(name = "tables")
@Data
@NoArgsConstructor
public class DiningTable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "table_number", nullable = false)
    private Integer tableNumber;

    private Integer seats;

    private Double x;
    private Double y;

    private String zone;

    private Boolean active = true;
}
