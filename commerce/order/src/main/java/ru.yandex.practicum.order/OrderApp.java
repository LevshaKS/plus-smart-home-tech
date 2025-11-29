package ru.yandex.practicum.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.interactionapi.feignClient.DeliveryFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.PaymentFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.ShoppingCartFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.WarehouseFeignClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(clients = {WarehouseFeignClient.class, DeliveryFeignClient.class, PaymentFeignClient.class, ShoppingCartFeignClient.class})
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class, args);
    }
}
