package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto orderDto);

    BigDecimal getTotalCost(OrderDto orderDto);

    void paymentRefund(UUID orderId);

    BigDecimal getProductCost(OrderDto orderDto);

    void paymentFailed(UUID orderId);
}
