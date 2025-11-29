package ru.yandex.practicum.interactionapi.feignClient;


import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interactionapi.model.*;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseFeignClient {


    @PutMapping
    void newProductInWarehouse(@RequestBody @Valid NewProductInWarehouseRequest requestDto);

    @PostMapping("/check")
    BookedProductDto checkProductQuantityCart(@RequestBody @Valid ShoppingCartDto shoppingCartDto);

    @PostMapping("/add")
    void addProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest requestDto);

    @GetMapping("/address")
    AddressDto getAddress();

    @PostMapping("/booking")
    BookedProductDto bookingProducts(@RequestBody @Valid ShoppingCartDto shoppingCartDto);

    @PostMapping("/shipped")
    void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest);

    @PostMapping("/return")
    void acceptReturn(@RequestBody Map<UUID, Long> products);

    @PostMapping("/assembly")
    BookedProductDto assemblyProductForOrder(@RequestBody @Valid AssemblyProductsForOrderRequest assemblyProductsForOrder);
}

