package ru.yandex.practicum.interactionapi.model;


import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.enums.ProductState;
import ru.yandex.practicum.interactionapi.enums.QuantityState;

import java.util.UUID;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {

    private UUID productId;
    @NotBlank
    private String productName;
    @NotBlank
    private String description;

    private String imageSrc;


    @NotNull
    private QuantityState quantityState;
    @NotNull
    private ProductState productState;

    private ProductCategory productCategory;

    @NotNull
    private double price;
}
