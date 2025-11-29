package ru.yandex.practicum.interactionapi.feignClient;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.model.CreateNewOrderRequest;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderFeignClient {

    @PutMapping
    OrderDto createNewOrder(@RequestBody @Valid CreateNewOrderRequest createNewOrderRequest);

    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId);

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId);


    @PostMapping("/calculate/delivery")
    OrderDto calculateDelivery(@RequestBody UUID orderId);


    @PostMapping("/calculate/delivery")
    OrderDto calculateTotal(@RequestBody UUID orderId);


    @GetMapping
    List<OrderDto> getClientOrders(@RequestParam String username);


    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId);

    @PostMapping("/assembly/failed")
    OrderDto assemblyFailed(@RequestBody UUID orderId);


    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId);

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId);

    @PostMapping("/completed")
    OrderDto completed(@RequestBody UUID orderId);

    @PostMapping("/return")
    OrderDto OrderReturn(@RequestBody @Valid ProductReturnRequest productReturnRequest);
}