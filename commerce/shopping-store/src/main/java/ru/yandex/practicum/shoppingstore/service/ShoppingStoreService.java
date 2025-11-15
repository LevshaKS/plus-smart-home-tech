package ru.yandex.practicum.shoppingstore.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.model.PageableDto;
import ru.yandex.practicum.interactionapi.model.ProductDto;
import ru.yandex.practicum.interactionapi.model.ProductQuantityStateRequest;


import java.util.List;
import java.util.UUID;


public interface ShoppingStoreService {

        List<ProductDto> getProducts(ProductCategory productCategory, PageableDto pageableDto);

        ProductDto createProduct(ProductDto productDto);

        ProductDto updateProduct(ProductDto productDto);

        boolean removeProduct(UUID productId);

        boolean setProductQuantityState(ProductQuantityStateRequest productQuantityStateRequest);

        ProductDto getProduct(UUID productId);
    }

