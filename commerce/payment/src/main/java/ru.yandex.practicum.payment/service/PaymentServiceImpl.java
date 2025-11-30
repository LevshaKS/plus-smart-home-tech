package ru.yandex.practicum.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.enums.PaymentState;
import ru.yandex.practicum.interactionapi.feignClient.OrderFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.ShoppingStoreFeignClient;
import ru.yandex.practicum.interactionapi.model.OrderDto;
import ru.yandex.practicum.interactionapi.model.PaymentDto;
import ru.yandex.practicum.interactionapi.model.ProductDto;
import ru.yandex.practicum.payment.dal.PaymentMapper;
import ru.yandex.practicum.payment.exception.NoPaymentFoundException;
import ru.yandex.practicum.payment.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.payment.model.Payment;
import ru.yandex.practicum.payment.repository.PaymentRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final ShoppingStoreFeignClient shoppingStoreFeignClient;
    private final OrderFeignClient orderFeignClient;

    @Override
    @Transactional
    public PaymentDto createPayment(OrderDto orderDto) {
        if (orderDto.getDeliveryPrice() == null || orderDto.getProductPrice() == null || orderDto.getTotalPrice() == null) {
            log.info("есть null данные для расчетов");
            throw new NotEnoughInfoInOrderToCalculateException("есть null данные для расчетов");
        }
        Payment payment = new Payment(null, orderDto.getOrderId(), orderDto.getTotalPrice(), orderDto.getDeliveryPrice(),
                orderDto.getProductPrice(), orderDto.getTotalPrice(), PaymentState.PENDING);
        log.info("создан платеж {}", payment);
        return paymentMapper.paymentToPaymentDto(payment);
    }

    @Override
    public BigDecimal getTotalCost(OrderDto orderDto) {
        if (orderDto.getDeliveryPrice() == null) {
            log.info("оплата имеет значение null");
            throw new NotEnoughInfoInOrderToCalculateException("оплата имеет значение null");
        }
        log.info("подсчет итогового платежа {}", orderDto);
        return orderDto.getProductPrice().add(orderDto.getDeliveryPrice()).add(orderDto.getProductPrice().multiply(BigDecimal.valueOf(0.1))); //+10% ндс
    }

    @Override
    @Transactional
    public void paymentRefund(UUID uuid) {
        Payment payment = paymentRepository.findById(uuid).orElseThrow(() -> new NoPaymentFoundException("платёж не найден"));
        payment.setStatus(PaymentState.SUCCESS);
        log.info("платеж прошел");
        orderFeignClient.payment(payment.getOrderId());
    }

    @Override
    public BigDecimal getProductCost(OrderDto orderDto) {
        BigDecimal result =BigDecimal.valueOf(0);
        Map<UUID, Long> products = orderDto.getProducts();
        if (products == null) {
            log.info("список null");
            throw new NotEnoughInfoInOrderToCalculateException("список null");
        }
        for (Map.Entry<UUID, Long> values : products.entrySet()) {
            ProductDto productDto = shoppingStoreFeignClient.getProduct(values.getKey());
            result = result.add(productDto.getPrice().multiply(BigDecimal.valueOf(values.getValue())));
        }
        log.info("расчет итога продуктов");
        return result;
    }

    @Override
    @Transactional
    public void paymentFailed(UUID uuid) {
        Payment payment = paymentRepository.findById(uuid).orElseThrow(() -> new NoPaymentFoundException("платёж не найден"));
        payment.setStatus(PaymentState.FAILED);
        log.info("ошибка платежа");
        orderFeignClient.paymentFailed(payment.getOrderId());

    }
}
