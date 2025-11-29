package ru.yandex.practicum.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.model.CreateNewOrderRequest;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.ProductReturnRequest;
import ru.yandex.practicum.order.service.OrderService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PutMapping
    public OrderDto createNewOrder(@RequestBody @Valid CreateNewOrderRequest createNewOrderRequest) {
        log.info("создние новго заказа {}", createNewOrderRequest);
        return orderService.createNewOrder(createNewOrderRequest);
    }

    @PostMapping("/payment")
    public OrderDto payment(@RequestBody UUID orderId) {
        log.info("оплата заказа {}", orderId);
        return orderService.payment(orderId);
    }

    @PostMapping("/payment/failed")
    public OrderDto paymentFailed(@RequestBody UUID orderId) {
        log.info("ошибка оплаты заказа {}", orderId);
        return orderService.paymentFailed(orderId);
    }

    @PostMapping("/calculate/delivery")
    public OrderDto calculateDelivery(@RequestBody UUID orderId) {
        log.info("расчет доставки {}", orderId);
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/calculate/total")
    public OrderDto calculateTotal(@RequestBody UUID orderId) {
        log.info("итоговый расчет заказа {}", orderId);
        return orderService.calculateTotal(orderId);
    }


    @GetMapping
    public List<OrderDto> getClientOrders(@RequestParam String username) {
        log.info("получение списка заказов пользователя {}", username);
        return orderService.getClientOrders(username);
    }

    @PostMapping("/assembly")
    public OrderDto assembly(@RequestBody UUID orderId) {
        log.info("сборка заказа {}", orderId);
        return orderService.assembly(orderId);
    }

    @PostMapping("/assembly/failed")
    public OrderDto assemblyFailed(@RequestBody UUID orderId) {
        log.info("ошибка сборки заказа {}", orderId);
        return orderService.assemblyFailed(orderId);
    }

    @PostMapping("/delivery")
    public OrderDto delivery(@RequestBody UUID orderId) {
        log.info("Доставка заказа {}", orderId);
        return orderService.delivery(orderId);
    }

    @PostMapping("/delivery/failed")
    public OrderDto deliveryFailed(@RequestBody UUID orderId) {
        log.info("ошибка доставки заказа {}", orderId);
        return orderService.deliveryFailed(orderId);
    }

    @PostMapping("/completed")
    public OrderDto completed(@RequestBody UUID orderId) {
        log.info("выполнение заказа {}", orderId);
        return orderService.completed(orderId);
    }

    @PostMapping("/return")
    public OrderDto OrderReturn(@RequestBody @Valid ProductReturnRequest productReturnRequest) {
        log.info("возврат заказа");
        return orderService.productReturn(productReturnRequest);
    }


}
