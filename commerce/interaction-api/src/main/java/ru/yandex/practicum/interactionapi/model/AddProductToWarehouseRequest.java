package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddProductToWarehouseRequest {
    @NotNull
    private UUID productId;
    @NotNull
    private Long quantity;
}
