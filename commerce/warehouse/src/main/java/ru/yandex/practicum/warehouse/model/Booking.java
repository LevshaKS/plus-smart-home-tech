package ru.yandex.practicum.warehouse.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    private UUID shoppingCartId;
    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;
    @ElementCollection
    @CollectionTable(name = "booking_products", joinColumns = @JoinColumn(name = "shopping_cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products;
    private UUID orderId;
    private UUID deliveryId;

}

