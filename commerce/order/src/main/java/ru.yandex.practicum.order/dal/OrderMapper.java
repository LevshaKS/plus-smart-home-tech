package ru.yandex.practicum.order.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.order.model.Order;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)

public interface OrderMapper {
    OrderDto orderToOrderDto(Order order);

    List<OrderDto> ordersToOrdersDto(List<Order> orders);

}
