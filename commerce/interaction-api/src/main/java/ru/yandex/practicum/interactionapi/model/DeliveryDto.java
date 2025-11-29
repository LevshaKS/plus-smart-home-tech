package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.interactionapi.enums.DeliveryState;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @NotNull
    private UUID deliveryId;
    @NotNull
    private AddressDto fromAddress;
    @NotNull
    private AddressDto toAddress;
    @NotNull
    private UUID orderId;
    @NotNull
    private DeliveryState deliveryState;
}
