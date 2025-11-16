package ru.yandex.practicum.shoppingcart.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@Entity
@ToString
@Table(name = "shopping_cart_user")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID shoppingCartId;
    String username;
    boolean active;

    @ElementCollection
    @CollectionTable(name = "shopping_cart_product", joinColumns = @JoinColumn(name = "cart_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    Map<UUID, Long> products;


}
