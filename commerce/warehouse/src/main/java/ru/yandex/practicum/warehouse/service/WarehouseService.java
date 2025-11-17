package ru.yandex.practicum.warehouse.service;

import ru.yandex.practicum.interactionapi.model.*;
import ru.yandex.practicum.warehouse.model.Warehouse;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface WarehouseService {
    void newProductInWarehouse(NewProductInWarehouseRequest newProductInWarehouse);

    BookedProductDto checkProductShoppingCart(ShoppingCartDto shoppingCartDto);

    void addProductToWarehouse(AddProductToWarehouseRequest requestDto);

    BookedProductDto getBookedProducts(Collection<Warehouse> productList,
                                       Map<UUID, Long> cartProducts);

    AddressDto getAddress();
}