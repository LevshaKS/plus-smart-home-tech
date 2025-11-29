package ru.yandex.practicum.order.service;

import ru.yandex.practicum.interactionapi.model.CreateNewOrderRequest;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.ProductReturnRequest;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    //новый заказ
    OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest);

    //оплата заказа
    OrderDto payment(UUID orderId);

    //расчет доставки
    OrderDto calculateDelivery(UUID orderId);

    //итоговый расчет
    OrderDto calculateTotal(UUID orderId);

    //получение списка заказов клиента
    List<OrderDto> getClientOrders(String username);//, Integer page, Integer size);

    //сборка заказа;
    OrderDto assembly(UUID orderId);

    //отгрузка товаров;
    OrderDto delivery(UUID orderId);

    //ошибка оплаты;
    OrderDto paymentFailed(UUID orderId);

    //ошибка сборки;
    OrderDto assemblyFailed(UUID orderId);

    //ошибка доставки;
    OrderDto deliveryFailed(UUID orderId);

    //возврат заказа
    OrderDto productReturn(ProductReturnRequest returnRequest);

    //заказ выполнен
    OrderDto completed(UUID orderId);

}
