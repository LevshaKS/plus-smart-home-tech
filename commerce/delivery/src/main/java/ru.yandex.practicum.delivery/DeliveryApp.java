package ru.yandex.practicum.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import ru.yandex.practicum.interactionapi.feignClient.OrderFeignClient;
import ru.yandex.practicum.interactionapi.feignClient.WarehouseFeignClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients(clients = {WarehouseFeignClient.class, OrderFeignClient.class})
public class DeliveryApp {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApp.class, args);
    }
}
