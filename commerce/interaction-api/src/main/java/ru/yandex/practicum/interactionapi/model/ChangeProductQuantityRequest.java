package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChangeProductQuantityRequest {
    @NotNull
    UUID productId;
    @NotNull
    @Min(0)
    Long newQuantity;
}
