package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.yandex.practicum.interactionapi.enums.OrderState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    @NotNull
    private UUID orderId;
    private UUID shoppingCartId;
    @NotNull
    private Map<UUID, Long> products;
    private UUID paymentId;
    private UUID deliveryId;

    private OrderState state;
    private double deliveryWeight;
    private double deliveryVolume;
    private boolean fragile;
    private BigDecimal totalPrice;
    private BigDecimal deliveryPrice;
    private BigDecimal productPrice;
}
