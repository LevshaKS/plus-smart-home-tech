package ru.yandex.practicum.interactionapi.feignClient;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.interactionapi.model.ShoppingCartDto;
import ru.yandex.practicum.interactionapi.model.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartFeignClient {

    @GetMapping
    ShoppingCartDto getCart(@RequestParam String user);

    @PutMapping
    ShoppingCartDto addProductToShoppingCart(@RequestParam String user, @RequestBody Map<UUID, Long> request);

    @PostMapping("/remove")
    ShoppingCartDto removeShoppingCart(@RequestParam String user, @RequestBody List<UUID> request);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String user, @Valid ChangeProductQuantityRequest request);


}
