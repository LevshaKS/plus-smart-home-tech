package ru.yandex.practicum.shoppingcart.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.feignClient.WarehouseFeignClient;
import ru.yandex.practicum.interactionapi.model.BookedProductDto;
import ru.yandex.practicum.interactionapi.model.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.model.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingcart.dal.CartMapper;
import ru.yandex.practicum.shoppingcart.exception.NoCartException;
import ru.yandex.practicum.shoppingcart.exception.NullAuthorizedException;
import ru.yandex.practicum.shoppingcart.model.Cart;
import ru.yandex.practicum.shoppingcart.repository.ShoppingCartRepository;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartMapper cartMapper;
    private final WarehouseFeignClient warehouseFeignClient;

    @Override
    public ShoppingCartDto getShoppingCart(String user) {
        if (user == null || user.isEmpty()) {
            throw new NullAuthorizedException("Имя пользователя пустое");
        }
        log.info("поиск корзины пользователя");
        return cartMapper.cartToShoppingCartDto(shoppingCartRepository.findByUsername(user));
    }

    @Override
    @Transactional
    public ShoppingCartDto addProductToCart(String user, Map<UUID, Long> request) {
        if (user == null || user.isEmpty()) {
            throw new NullAuthorizedException("Имя пользователя пустое");
        }
        Cart cart = new Cart(null, user, true, request);
        log.info("добавляем новую корзину пользователя {}", cart);
        return cartMapper.cartToShoppingCartDto(shoppingCartRepository.save(cart));

    }

    @Override
    @Transactional
    public void deactivateCart(String user) {
        if (user == null || user.isEmpty()) {
            throw new NullAuthorizedException("Имя пользователя пустое");
        }
        Cart cart = shoppingCartRepository.findByUsername(user);
        cart.setActive(false);
        log.info("деактивировали корзину пользователя");
        shoppingCartRepository.save(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeCart(String user, List<UUID> request) {
        if (user == null || user.isEmpty()) {
            throw new NullAuthorizedException("Имя пользователя пустое");
        }
        Cart cart = shoppingCartRepository.findByUsername(user);
        if (cart == null) {
            throw new NoCartException(user + " нет козрины с покупками");
        }
        Map<UUID, Long> products = cart.getProducts();
        request.forEach(products.keySet()::remove);

        cart.setProducts(products);

        log.info("удаляем товар из корзины");
        shoppingCartRepository.save(cart);
        return cartMapper.cartToShoppingCartDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeProductQuantity(String user, ChangeProductQuantityRequest request) {
        if (user == null || user.isEmpty()) {
            throw new NullAuthorizedException("Имя пользователя пустое");
        }
        Cart cart = shoppingCartRepository.findByUsername(user);
        cart.getProducts().entrySet().stream()
                .filter(uuidLongEntry -> uuidLongEntry.getKey().equals(request.getProductId()))
                .peek(uuidLongEntry -> uuidLongEntry.setValue(request.getNewQuantity()))
                .findAny();

        cart = shoppingCartRepository.save(cart);
        log.info("изменяем колличество товара в корзине {}", cart);

        return cartMapper.cartToShoppingCartDto(cart);
    }

    @Override
    public BookedProductDto bookingProducts(String user) {
        if (user == null || user.isEmpty()) {
            throw new NullAuthorizedException("Имя пользователя пустое");
        }
        Cart cart = shoppingCartRepository.findByUsername(user);
        return warehouseFeignClient.bookingProducts(cartMapper.cartToShoppingCartDto(cart));
    }
}
