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
    UUID productId;
    long quantity;
    Boolean fragile;
    @Embedded
    Dimension dimension;
    double weight;
}
