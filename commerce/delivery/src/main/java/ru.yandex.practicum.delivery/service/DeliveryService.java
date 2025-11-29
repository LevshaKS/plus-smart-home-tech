package ru.yandex.practicum.delivery.service;

import ru.yandex.practicum.interactionapi.model.DeliveryDto;
import ru.yandex.practicum.interactionapi.model.OrderDto;

import java.util.UUID;

public interface DeliveryService {

    DeliveryDto createNewDelivery(DeliveryDto deliveryDto);

    void deliverySuccessful(UUID deliveryId);

    void deliveryPicked(UUID deliveryId);

    void deliveryFailed(UUID deliveryId);

    Double deliveryCost(OrderDto orderDto);
}
