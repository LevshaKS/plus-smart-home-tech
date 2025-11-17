package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookedProductDto {
    @NotNull
    private Double deliveryWeight;
    @NotNull
    private Double deliveryVolume;
    @NotNull
    private Boolean fragile;
}
