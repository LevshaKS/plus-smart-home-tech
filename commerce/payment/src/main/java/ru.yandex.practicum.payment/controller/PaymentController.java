package ru.yandex.practicum.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.PaymentDto;
import ru.yandex.practicum.payment.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody @Valid OrderDto orderDto) {
        log.info("создаем платеж заказа {}", orderDto);
        return paymentService.createPayment(orderDto);
    }

    @PostMapping("/totalCost")
    public BigDecimal getTotalCost(@RequestBody @Valid OrderDto orderDto) {
        log.info("расчет суммы платежа заказа {}", orderDto);
        return paymentService.getTotalCost(orderDto);
    }

    @PostMapping("/refund")
    public void paymentRefund(@RequestBody UUID orderId) {
        log.info("успешная оплата заказа {}", orderId);
        paymentService.paymentRefund(orderId);
    }

    @PostMapping("/productCost")
    public BigDecimal getProductCost(@RequestBody @Valid OrderDto orderDto) {
        log.info("расчет суммы платежа товара {}", orderDto);
        return paymentService.getProductCost(orderDto);

    }

    @PostMapping("/failed")
    public void paymentFailed(@RequestBody UUID orderId) {
        log.info("ошибка оплаты заказа {}", orderId);
        paymentService.paymentFailed(orderId);
    }


}
