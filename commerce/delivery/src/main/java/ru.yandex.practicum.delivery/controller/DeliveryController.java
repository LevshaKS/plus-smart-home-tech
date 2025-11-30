package ru.yandex.practicum.delivery.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.delivery.service.DeliveryService;
import ru.yandex.practicum.interactionapi.model.DeliveryDto;
import ru.yandex.practicum.interactionapi.model.OrderDto;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PutMapping
    public DeliveryDto createNewDelivery(@RequestBody @Valid DeliveryDto deliveryDto) {
        log.info("новая доставка {}", deliveryDto);
        return deliveryService.createNewDelivery(deliveryDto);
    }

    @PostMapping("/successful")
    public void deliverySuccessful(@RequestBody UUID deliveryId) {
        log.info("выполнена доставка {}", deliveryId);
        deliveryService.deliverySuccessful(deliveryId);
    }

    @PostMapping("/picked")
    public void deliveryPicked(@RequestBody UUID deliveryId) {
        log.info("получена доставка {}", deliveryId);
        deliveryService.deliveryPicked(deliveryId);
    }

    @PostMapping("/failed")
    public void deliveryFailed(@RequestBody UUID deliveryId) {
        log.info("неудачная доставка {}", deliveryId);
        deliveryService.deliveryFailed(deliveryId);
    }

    @PostMapping("/cost")
    public BigDecimal deliveryCost(@RequestBody @Valid OrderDto orderDto) {
        log.info("расчет доставки заказа {}", orderDto);
        return deliveryService.deliveryCost(orderDto);
    }
}
