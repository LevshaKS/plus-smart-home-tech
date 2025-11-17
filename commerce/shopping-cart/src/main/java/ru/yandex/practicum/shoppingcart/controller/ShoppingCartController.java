package ru.yandex.practicum.shoppingcart.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.model.BookedProductDto;
import ru.yandex.practicum.interactionapi.model.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.model.ChangeProductQuantityRequest;
import ru.yandex.practicum.shoppingcart.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ShoppingCartDto getShoppingCart(@RequestParam String username) {

        log.info("получение корзины пользователя " + username);
        return shoppingCartService.getShoppingCart(username);
    }

    @PutMapping
    public ShoppingCartDto addProductToCart(@RequestParam String username, @RequestBody Map<UUID, Long> request) {
        log.info("добавление товара в корзину пользователя " + username);
        return shoppingCartService.addProductToCart(username, request);
    }

    @DeleteMapping
    public void deactivateCart(@RequestParam String username) {
        log.info("деактивация корзины пользователя " + username);
        shoppingCartService.deactivateCart(username);
    }

    @PostMapping("/remove")
    public ShoppingCartDto removeCart(@RequestParam String username, @RequestBody List<UUID> request) {
        log.info("изменение наименования товара в корзине " + username);
        return shoppingCartService.removeCart(username, request);
    }

    @PostMapping("/change-quantity")
    private ShoppingCartDto changeProductQuantity(@RequestParam String username, @RequestBody ChangeProductQuantityRequest request) {
        log.info("изменение колличество товара в корзине " + username);

        return shoppingCartService.changeProductQuantity(username, request);
    }

    @PostMapping("/booking")
    public BookedProductDto bookingProduct(@RequestParam String username) {
        log.info("Бронирование корзины покупок для пользователя {}", username);
        return shoppingCartService.bookingProducts(username);
    }


}
