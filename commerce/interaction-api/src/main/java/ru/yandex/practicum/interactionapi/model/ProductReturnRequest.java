package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductReturnRequest {
    @NotNull
    private UUID orderId;
    @NotNull
    private Map<UUID, Long> products;
}
