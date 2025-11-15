package ru.yandex.practicum.shoppingstore.model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.enums.ProductState;
import ru.yandex.practicum.interactionapi.enums.QuantityState;

import java.util.UUID;


@Getter
@Transactional
@Setter
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    //id продукта в формате uuid
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID productId;

    String productName;
    String description;
    String imageSrc;
    double price;

    @Enumerated(EnumType.STRING)
    QuantityState quantityState;

    @Enumerated(EnumType.STRING)
    ProductState productState;

    @Enumerated(EnumType.STRING)
    ProductCategory productCategory;

}
