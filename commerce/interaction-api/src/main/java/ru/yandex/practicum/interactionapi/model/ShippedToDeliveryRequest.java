package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ShippedToDeliveryRequest {

    @NotNull
    private UUID orderId;
    @NotNull
    private UUID deliveryId;
}
