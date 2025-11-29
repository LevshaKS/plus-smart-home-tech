package ru.yandex.practicum.interactionapi.feignClient;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.PaymentDto;

import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentFeignClient {

    @PostMapping
    PaymentDto createPayment(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/totalCost")
    Double getTotalCost(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/refund")
    void paymentRefund(@RequestBody UUID orderId);

    @PostMapping("/productCost")
    Double getProductCost(@RequestBody @Valid OrderDto orderDto);

    @PostMapping("/failed")
    void paymentFailed(@RequestBody UUID orderId);

}
