package ru.yandex.practicum.interactionapi.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.model.BookedProductDto;
import ru.yandex.practicum.interactionapi.model.ChangeProductQuantityRequest;
import ru.yandex.practicum.interactionapi.model.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartFeignClient {

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam String username);

    @PutMapping
    ShoppingCartDto addProductToCart(@RequestParam String username, @RequestBody Map<UUID, Long> request);

    @DeleteMapping
    void deactivateCart(@RequestParam String username);

    @PostMapping("/remove")
    ShoppingCartDto removeCart(@RequestParam String username, @RequestBody List<UUID> request);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeProductQuantity(@RequestParam String username, @RequestBody ChangeProductQuantityRequest request);

    @PostMapping("/booking")
    BookedProductDto bookingProduct(@RequestParam String username);
}
