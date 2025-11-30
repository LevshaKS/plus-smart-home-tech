package ru.yandex.practicum.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.delivery.dal.DeliveryMapper;
import ru.yandex.practicum.delivery.exception.NoDeliveryFoundException;
import ru.yandex.practicum.delivery.model.Delivery;
import ru.yandex.practicum.delivery.repository.DeliveryRepository;
import ru.yandex.practicum.interactionapi.enums.DeliveryState;
import ru.yandex.practicum.interactionapi.feignClient.OrderFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.WarehouseFeignClient;
import ru.yandex.practicum.interactionapi.model.AddressDto;
import ru.yandex.practicum.interactionapi.model.DeliveryDto;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryMapper deliveryMapper;
    private final DeliveryRepository deliveryRepository;
    private final OrderFeignClient orderFeignClient;
    private final WarehouseFeignClient warehouseFeignClient;

    private final double base = 5.0;

    @Override
    @Transactional
    public DeliveryDto createNewDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = deliveryMapper.deliveryDtoToDelivery(deliveryDto);
        delivery.setDeliveryState(DeliveryState.CREATED);
        log.info("добавили новую доставку {}", delivery);
        return deliveryMapper.deliveryTodeliveryDto(deliveryRepository.save(delivery));
    }

    @Override
    @Transactional
    public void deliverySuccessful(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NoDeliveryFoundException("нет такой доставки " + deliveryId));
        delivery.setDeliveryState(DeliveryState.DELIVERED);
        log.info("Доставка выполнена {}", delivery);
        orderFeignClient.completed(delivery.getOrderId());
    }

    @Override
    @Transactional
    public void deliveryPicked(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NoDeliveryFoundException("нет такой доставки " + deliveryId));
        delivery.setDeliveryState(DeliveryState.IN_PROGRESS);
        log.info("предано в доставку {}", delivery);
        orderFeignClient.assembly(deliveryId);
        warehouseFeignClient.shippedToDelivery(new ShippedToDeliveryRequest(delivery.getOrderId(), delivery.getDeliveryId()));
    }

    @Override
    @Transactional
    public void deliveryFailed(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new NoDeliveryFoundException("нет такой доставки " + deliveryId));
        delivery.setDeliveryState(DeliveryState.FAILED);
        log.info("ошибка при доставке {}", delivery);
        orderFeignClient.deliveryFailed(delivery.getOrderId());
    }

    @Override
    public BigDecimal deliveryCost(OrderDto orderDto) {
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getDeliveryId()).orElseThrow(() -> new NoDeliveryFoundException("нет такой доставки "));
        AddressDto addressDto = warehouseFeignClient.getAddress();
        //Умножаем базовую стоимость на число, зависящее от адреса склада
        BigDecimal addressCost = BigDecimal.valueOf(base);
        switch (addressDto.getCity()) {
            case "ADDRESS_1":
                //Если адрес склада содержит название ADDRESS_1, то умножаем на 1.
                addressCost = addressCost.multiply(BigDecimal.ONE);
                log.info("id заказа: " + delivery.getOrderId() + " Доставка со склада адреса 1 базовый коофицент * на 1 {}", addressCost);
                break;
            case "ADDRESS_2":
                //Если адрес склада содержит название ADDRESS_2, то умножаем на 2.
                addressCost = addressCost.multiply(BigDecimal.TWO);
                log.info("id заказа: " + delivery.getOrderId() + " Доставка со склада адреса 2 базовый коофицент * на 2 {}", addressCost);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + addressDto.getCity());
        }
        // Складываем получившийся результат с базовой стоимостью.
        BigDecimal deliveryCost = addressCost.add(BigDecimal.valueOf(base));
        log.info("id заказа: " + delivery.getOrderId() + " Складываем получившийся результат с базовой стоимости доставки  {}", deliveryCost);

        if (orderDto.isFragile()) {
            //если хрупкое то результат умнажаем на 0.2
            deliveryCost = deliveryCost.add(deliveryCost.multiply(BigDecimal.valueOf(0.2)));
            log.info("id заказа: " + delivery.getOrderId() + " Добавили коофицент 0.2 за хрупкойсть к стоимости доставки {}", deliveryCost);
        }
//Добавляем к сумме, полученной на предыдущих шагах, вес заказа, умноженный на 0.3
        deliveryCost = deliveryCost.add(deliveryCost.multiply(BigDecimal.valueOf(orderDto.getDeliveryWeight() * 0.3)));
        log.info("id заказа: " + delivery.getOrderId() + " Добавили к итогу вес заказа * на коофицент 0.3 {}", deliveryCost);

//Складываем с полученным на прошлом шаге итогом объём, умноженный на 0.2
        deliveryCost = deliveryCost.add(deliveryCost.multiply(BigDecimal.valueOf(orderDto.getDeliveryVolume() * 0.2)));
        log.info("id заказа: " + delivery.getOrderId() + " Добавили к итогу объем заказа * на коофицент 0.2 {}", deliveryCost);

        if (!addressDto.getStreet().equals(delivery.getToAddress().getStreet())) {
            // доставка на улицу не на ту же где склад
            deliveryCost = deliveryCost.add(deliveryCost.multiply(BigDecimal.valueOf(0.2)));
            log.info("id заказа: " + delivery.getOrderId() + " Доставка на улицу не на ту же где склад {}", deliveryCost);
        }
        log.info("id заказа: " + delivery.getOrderId() + " Расчет доставки {}", deliveryCost);
        return deliveryCost;
    }
}
