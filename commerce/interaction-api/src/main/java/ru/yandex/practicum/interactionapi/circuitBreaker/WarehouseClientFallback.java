package ru.yandex.practicum.interactionapi.circuitBreaker;

import ru.yandex.practicum.interactionapi.feignClient.WarehouseFeignClient;
import ru.yandex.practicum.interactionapi.model.*;

import java.util.Map;
import java.util.UUID;

public class WarehouseClientFallback implements WarehouseFeignClient {
    @Override
    public void newProductInWarehouse(NewProductInWarehouseRequest requestDto) {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }

    @Override
    public BookedProductDto checkProductQuantityCart(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }

    @Override
    public void addProductToWarehouse(AddProductToWarehouseRequest requestDto) {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }

    @Override
    public AddressDto getAddress() {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }

    @Override
    public BookedProductDto bookingProducts(ShoppingCartDto shoppingCartDto) {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest) {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }

    @Override
    public void acceptReturn(Map<UUID, Long> products) {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }

    @Override
    public BookedProductDto assemblyProductForOrder(AssemblyProductsForOrderRequest assemblyProductsForOrder) {
        throw new WarehouseServerError("Cервис временно недоступен.");
    }
}
