package ru.yandex.practicum.interactionapi.feignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interactionapi.model.DeliveryDto;
import ru.yandex.practicum.interactionapi.model.OrderDto;

import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryFeignClient {

    @PutMapping
    DeliveryDto createNewDelivery(@RequestBody @Valid DeliveryDto deliveryDto);

    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody UUID deliveryId);

    @PostMapping("/picked")
    void deliveryPicked(@RequestBody UUID deliveryId);

    @PostMapping("/failed")
    void deliveryFailed(@RequestBody UUID deliveryId);

    @PostMapping("/cost")
    Double deliveryCost(@RequestBody @Valid OrderDto orderDto);
}
