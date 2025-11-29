package ru.yandex.practicum.payment.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interactionapi.model.PaymentDto;
import ru.yandex.practicum.payment.model.Payment;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    PaymentDto paymentToPaymentDto(Payment payment);
}
