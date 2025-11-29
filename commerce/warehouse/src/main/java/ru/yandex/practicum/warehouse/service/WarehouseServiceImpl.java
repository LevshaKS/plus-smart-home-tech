package ru.yandex.practicum.warehouse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.enums.QuantityState;
import ru.yandex.practicum.interactionapi.feignClient.ShoppingStoreFeignClient;
import ru.yandex.practicum.interactionapi.model.*;
import ru.yandex.practicum.warehouse.address.Address;
import ru.yandex.practicum.warehouse.dal.BookingMapper;
import ru.yandex.practicum.warehouse.dal.WarehouseMapper;
import ru.yandex.practicum.warehouse.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.warehouse.exception.NotFoundProductInWareHouseException;
import ru.yandex.practicum.warehouse.exception.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.yandex.practicum.warehouse.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.warehouse.model.Booking;
import ru.yandex.practicum.warehouse.model.Warehouse;
import ru.yandex.practicum.warehouse.repository.BookingRepository;
import ru.yandex.practicum.warehouse.repository.WarehouseRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final ShoppingStoreFeignClient shoppingStoreFeignClient;
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public void newProductInWarehouse(NewProductInWarehouseRequest newProductInWarehouse) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(newProductInWarehouse.getProductId());
        if (warehouse.isPresent()) {
            log.warn("такой товар уже есть {}", newProductInWarehouse);
            throw new SpecifiedProductAlreadyInWarehouseException("такой товар уже есть");
        }
        Warehouse newWarehouse = warehouseMapper.newProductToWarehouse(newProductInWarehouse);
        log.info("добавили новый товар на склад {}", newWarehouse);
        warehouseRepository.save(newWarehouse);
    }

    @Override

    public BookedProductDto checkProductShoppingCart(ShoppingCartDto shoppingCartDto) {
        Map<UUID, Long> products = shoppingCartDto.getProducts();
        Set<UUID> cartProductIds = products.keySet();
        Map<UUID, Warehouse> warehouseProducts = warehouseRepository.findAllById(cartProductIds)
                .stream()
                .collect(Collectors.toMap(Warehouse::getProductId, Function.identity()));
        Set<UUID> productIds = warehouseProducts.keySet();
        cartProductIds.forEach(uuid -> {
            if (!productIds.contains(uuid)) {
                throw new NotFoundProductInWareHouseException("товара нет на складе");
            }
        });
        products.forEach((uuid, aLong) -> {
            if (warehouseProducts.get(uuid).getQuantity() < aLong) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException("не достаточно товара на складе");
            }
        });
        return getBookedProducts(warehouseProducts.values(), products);
    }

    @Override
    @Transactional
    public void addProductToWarehouse(AddProductToWarehouseRequest addProductInWarehouse) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(addProductInWarehouse.getProductId());
        if (warehouse.isEmpty()) {
            log.warn("такого товара нет в базе на складе {}", addProductInWarehouse);
            throw new NoSpecifiedProductInWarehouseException("такого товара нет в базе");
        }
        warehouse.get().setQuantity(warehouse.get().getQuantity() + addProductInWarehouse.getQuantity());
        log.info("добавили колличество товара на склад {}", addProductInWarehouse);
        warehouseRepository.save(warehouse.get());
        if (warehouse.get().getQuantity() == 0) {
            shoppingStoreFeignClient.setProductQuantityState(warehouse.get().getProductId(), QuantityState.ENDED);
        } else if (warehouse.get().getQuantity() < 10) {
            shoppingStoreFeignClient.setProductQuantityState(warehouse.get().getProductId(), QuantityState.ENOUGH);
        } else if (warehouse.get().getQuantity() < 100) {
            shoppingStoreFeignClient.setProductQuantityState(warehouse.get().getProductId(), QuantityState.FEW);
        } else {
            shoppingStoreFeignClient.setProductQuantityState(warehouse.get().getProductId(), QuantityState.MANY);
        }

    }

    @Override
    public BookedProductDto getBookedProducts(Collection<Warehouse> productList, Map<UUID, Long> cartProducts) {
        BookedProductDto bookedProductDto = new BookedProductDto();
        bookedProductDto.setFragile(productList.stream().anyMatch(Warehouse::getFragile));
        bookedProductDto.setDeliveryWeight(productList.stream()
                .mapToDouble(value -> value.getWeight() * cartProducts.get(value.getProductId())).sum());
        bookedProductDto.setDeliveryVolume(productList.stream()
                .mapToDouble(value -> value.getDimension().getWidth() * value.getDimension().getHeight() * value.getDimension().getDepth()
                        * cartProducts.get(value.getProductId())).sum());
        log.info("получение товара");
        return bookedProductDto;
    }

    @Override
    public AddressDto getAddress() {
        String address = Address.CURRENT_ADDRESS;
        return new AddressDto(address, address, address, address, address);
    }

    @Override
    @Transactional
    public void shippedToDelivery(ShippedToDeliveryRequest deliveryRequest) {
        Booking booking = bookingRepository.findByOrderId(deliveryRequest.getOrderId())
                .orElseThrow(() -> new NoSpecifiedProductInWarehouseException("не найден на складе"));
        log.info("передан товар в доставку");
        booking.setDeliveryId(deliveryRequest.getDeliveryId());
    }

    @Override
    @Transactional
    public void acceptReturn(Map<UUID, Long> products) {
        List<Warehouse> warehouses = warehouseRepository.findAllById(products.keySet());
        for (Warehouse warehouse : warehouses) {
            warehouse.setQuantity(warehouse.getQuantity() + products.get(warehouse.getProductId()));
        }
        log.info("возвращен товар на склад {}", products);
    }

    @Override
    @Transactional
    public BookedProductDto assemblyProductForOrder(AssemblyProductsForOrderRequest assemblyProductsForOrder) {
        Booking booking = bookingRepository.findById(assemblyProductsForOrder.getShoppingCartId()).orElseThrow(() ->
                new NotFoundProductInWareHouseException("не найден в базе " + assemblyProductsForOrder.getShoppingCartId()));

        Map<UUID, Long> products = booking.getProducts();
        List<Warehouse> productInWarehouse = warehouseRepository.findAllById(products.keySet());
        for (Warehouse warehouse : productInWarehouse) {
            if (warehouse.getQuantity() < products.get(warehouse.getProductId())) {
                throw new ProductInShoppingCartLowQuantityInWarehouseException("не хватает количества товара на складе");
            }
            warehouse.setQuantity(warehouse.getQuantity() - products.get(warehouse.getProductId()));
        }
        booking.setOrderId(assemblyProductsForOrder.getOrderId());
        log.info("сборка товара для отправки {}", assemblyProductsForOrder);
        return bookingMapper.productToBookingProductDto(booking);
    }


}

