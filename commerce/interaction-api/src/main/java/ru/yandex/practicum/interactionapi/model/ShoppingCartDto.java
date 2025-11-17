package ru.yandex.practicum.interactionapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ShoppingCartDto {
    @NotNull
    private UUID shoppingCartId;
    @NotNull
    private Map<UUID, Long> products;
}
