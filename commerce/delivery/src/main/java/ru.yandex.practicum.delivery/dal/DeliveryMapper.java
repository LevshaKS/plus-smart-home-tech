package ru.yandex.practicum.delivery.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.interactionapi.model.DeliveryDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DeliveryMapper {

    DeliveryDto deliveryTodeliveryDto(Delivery delivery);

    Delivery deliveryDtoToDelivery(DeliveryDto deliveryDto);
}
