package ru.yandex.practicum.interactionapi.model;


import jakarta.validation.constraints.*;

import lombok.*;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.enums.ProductState;
import ru.yandex.practicum.interactionapi.enums.QuantityState;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {

    UUID productId;
    @NotBlank
    String productName;
    @NotBlank
    String description;

    String imageSrc;


    @NotNull
    QuantityState quantityState;
    @NotNull
    ProductState productState;

    ProductCategory productCategory;

    @NotNull
    double price;
}
