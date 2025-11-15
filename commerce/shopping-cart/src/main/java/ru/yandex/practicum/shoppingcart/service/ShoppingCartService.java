package ru.yandex.practicum.shoppingcart.service;


import ru.yandex.practicum.interactionapi.model.BookedProductDto;
import ru.yandex.practicum.interactionapi.model.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.model.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String user);

    ShoppingCartDto addProductToCart(String user, Map<UUID, Long> request);

    void deactivateCart(String user);

    ShoppingCartDto removeCart(String user, List<UUID> request);

    ShoppingCartDto changeProductQuantity(String user, ChangeProductQuantityRequest request);

    BookedProductDto bookingProducts(String user);
}
