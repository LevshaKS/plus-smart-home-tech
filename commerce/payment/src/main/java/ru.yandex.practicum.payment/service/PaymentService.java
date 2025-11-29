package ru.yandex.practicum.payment.service;

import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto orderDto);

    Double getTotalCost(OrderDto orderDto);

    void paymentRefund(UUID orderId);

    Double getProductCost(OrderDto orderDto);

    void paymentFailed(UUID orderId);
}
