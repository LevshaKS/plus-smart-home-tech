package ru.yandex.practicum.warehouse.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.model.*;
import ru.yandex.practicum.warehouse.service.WarehouseService;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @PutMapping
    public void newProduct(@RequestBody @Valid NewProductInWarehouseRequest product) {
        log.info("добавление нового продукта {}", product);
        warehouseService.newProductInWarehouse(product);
    }

    @PostMapping("/add")
    public void addProduct(@RequestBody @Valid AddProductToWarehouseRequest product) {
        log.info("добавление колличество продукта {}", product);
        warehouseService.addProductToWarehouse(product);
    }

    @GetMapping("/address")
    public AddressDto getAddress() {
        log.info("Получаем адрес");
        return warehouseService.getAddress();
    }

    @PostMapping("/check")
    public BookedProductDto checkProductQuantityEnoughForShoppingCart(@RequestBody @Valid ShoppingCartDto shoppingCartDto) {
        log.info("Проверка достаточности продуктов {}", shoppingCartDto);
        return warehouseService.checkProductShoppingCart(shoppingCartDto);
    }

}
