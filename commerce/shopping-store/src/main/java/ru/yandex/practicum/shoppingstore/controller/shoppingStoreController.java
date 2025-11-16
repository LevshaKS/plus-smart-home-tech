package ru.yandex.practicum.shoppingstore.controller;


import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.model.PageableDto;
import ru.yandex.practicum.interactionapi.model.ProductDto;
import ru.yandex.practicum.interactionapi.model.ProductQuantityStateRequest;
import ru.yandex.practicum.interactionapi.model.ProductsPageDto;
import ru.yandex.practicum.shoppingstore.service.ShoppingStoreService;


import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/shopping-store")
@RequiredArgsConstructor
public class shoppingStoreController {

    private final ShoppingStoreService shoppingStoreService;

    @PutMapping
    public ProductDto createProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("добавляем новый продукт {}", productDto);
        return shoppingStoreService.createProduct(productDto);
    }

    @PostMapping
    public ProductDto updateProduct(@RequestBody @Valid ProductDto productDto) {
        log.info("обновление продукта {}", productDto);
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    public Boolean removeProduct(@RequestBody @NonNull UUID productId) {
        log.info("деактивация продукта {} из асортимента", productId);
        return shoppingStoreService.removeProduct(productId);
    }

    @GetMapping("/{productId}")
    public ProductDto getProduct(@PathVariable @NonNull UUID productId) {
        log.info("поиск продукта {}", productId);
        return shoppingStoreService.getProduct(productId);
    }

    @GetMapping
    public ProductsPageDto getProducts(@RequestParam ProductCategory category, @Valid PageableDto pageableDto) {
        log.info("поиск категории продукта {}", category);
        return shoppingStoreService.getProducts(category, pageableDto);
    }

    @PostMapping("/quantityState")
    public Boolean setProductQuantityState(ProductQuantityStateRequest productQuantityStateRequest) {
        log.info("статус товара {}", productQuantityStateRequest);
        return shoppingStoreService.setProductQuantityState(productQuantityStateRequest);
    }

}
