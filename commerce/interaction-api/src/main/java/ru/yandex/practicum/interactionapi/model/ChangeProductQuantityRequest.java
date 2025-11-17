package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;
@FieldDefaults(level = AccessLevel.PRIVATE)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChangeProductQuantityRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @Min(0)
    private Long newQuantity;
}
