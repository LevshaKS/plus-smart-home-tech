package ru.yandex.practicum.order.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.enums.OrderState;
import ru.yandex.practicum.interactionapi.feignClient.DeliveryFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.PaymentFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.ShoppingCartFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.WarehouseFeignClient;
import ru.yandex.practicum.interactionapi.model.*;
import ru.yandex.practicum.order.dal.OrderMapper;
import ru.yandex.practicum.order.exception.NoOrderFoundException;
import ru.yandex.practicum.order.exception.NotAuthorizedUserException;
import ru.yandex.practicum.order.model.Order;
import ru.yandex.practicum.order.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseFeignClient warehouseFeignClient;
    private final ShoppingCartFeignClient shoppingCartFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final DeliveryFeignClient deliveryFeignClient;

    @Override
    @Transactional
    public OrderDto createNewOrder(CreateNewOrderRequest createNewOrderRequest) {
        Order order = new Order(null,
                createNewOrderRequest.getShoppingCart().getShoppingCartId(),
                createNewOrderRequest.getShoppingCart().getProducts(),
                null, null,
                OrderState.NEW,
                null, null, null, null, null, null);

        order = orderRepository.save(order);

        BookedProductDto bookedProductDto = warehouseFeignClient.assemblyProductForOrder(
                new AssemblyProductsForOrderRequest(createNewOrderRequest.getShoppingCart().getShoppingCartId(), order.getOrderId()));
        order.setFragile(bookedProductDto.getFragile());
        order.setDeliveryVolume(bookedProductDto.getDeliveryVolume());
        order.setDeliveryWeight(bookedProductDto.getDeliveryWeight());
        order.setProductPrice(paymentFeignClient.getProductCost(orderMapper.orderToOrderDto(order)));

        DeliveryDto deliveryDto = new DeliveryDto(
                null,
                warehouseFeignClient.getAddress()
                , createNewOrderRequest.getDeliveryAddress()
                , order.getOrderId()
                , null);
        order.setDeliveryId(deliveryFeignClient.createNewDelivery(deliveryDto).getDeliveryId());
        paymentFeignClient.createPayment(orderMapper.orderToOrderDto(order));
        log.info("добавили новый заказ");
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto payment(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setState(OrderState.PAID);
        log.info(" оплата заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto calculateDelivery(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setDeliveryPrice(deliveryFeignClient.deliveryCost(orderMapper.orderToOrderDto(order)));
        log.info(" расчет  доставки {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto calculateTotal(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setTotalPrice(paymentFeignClient.getTotalCost(orderMapper.orderToOrderDto(order)));
        log.info("итоговый расчет заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    public List<OrderDto> getClientOrders(String username) {
        if (username.isEmpty()) {
            throw new NotAuthorizedUserException("имя пользователя пустое");
        }
        ShoppingCartDto shoppingCartDto = shoppingCartFeignClient.getShoppingCart(username);

        List<Order> orders = orderRepository.findByShoppingCartId(shoppingCartDto.getShoppingCartId());
        log.info("получение списка заказов пользователя {}", username);
        return orderMapper.ordersToOrdersDto(orders);
    }

    @Override
    @Transactional
    public OrderDto assembly(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setState(OrderState.ASSEMBLED);
        log.info(" сборка заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto delivery(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setState(OrderState.DELIVERED);
        log.info("доставка заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto paymentFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setState(OrderState.PAYMENT_FAILED);
        log.info("ошибка оплаты заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto assemblyFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setState(OrderState.ASSEMBLY_FAILED);
        log.info("ошибка сборки заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto deliveryFailed(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setState(OrderState.DELIVERY_FAILED);
        log.info("ошибка сборки заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto productReturn(ProductReturnRequest returnRequest) {
        Order order = orderRepository.findById(returnRequest.getOrderId()).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        warehouseFeignClient.acceptReturn(returnRequest.getProducts());
        order.setState(OrderState.PRODUCT_RETURNED);
        log.info("возврат заказа {}", returnRequest);
        return orderMapper.orderToOrderDto(order);
    }

    @Override
    @Transactional
    public OrderDto completed(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new NoOrderFoundException("Заказ не найден"));
        order.setState(OrderState.COMPLETED);
        log.info("выполнение заказа {}", orderId);
        return orderMapper.orderToOrderDto(order);
    }
}
