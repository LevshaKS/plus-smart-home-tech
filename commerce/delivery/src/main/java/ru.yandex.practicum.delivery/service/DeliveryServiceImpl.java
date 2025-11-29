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
    public Double deliveryCost(OrderDto orderDto) {
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getDeliveryId()).orElseThrow(() -> new NoDeliveryFoundException("нет такой доставки "));
        AddressDto addressDto = warehouseFeignClient.getAddress();
        double addressCost = 0.0;
        switch (addressDto.getCity()) {
            case "ADDRESS_1":
                addressCost = base;
                break;
            case "ADDRESS_2":
                addressCost = base * 2;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + addressDto.getCity());
        }
        double deliveryCost = base + addressCost;
        if (orderDto.isFragile()) {
            deliveryCost += deliveryCost * 0.2;
        }
        deliveryCost += orderDto.getDeliveryWeight() * 0.3;
        deliveryCost += orderDto.getDeliveryVolume() * 0.2;
        if (!addressDto.getStreet().equals(delivery.getToAddress().getStreet())) {
            deliveryCost += deliveryCost * 0.2;
        }
        log.info("расчет доставки {}", deliveryCost);
        return deliveryCost;
    }
}
