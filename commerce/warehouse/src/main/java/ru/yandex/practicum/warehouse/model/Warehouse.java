package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "warehouse_product")
public class Warehouse {
    @Id
    private UUID productId;
    private long quantity;
    private Boolean fragile;
    @Embedded
    private Dimension dimension;
    private double weight;
}
