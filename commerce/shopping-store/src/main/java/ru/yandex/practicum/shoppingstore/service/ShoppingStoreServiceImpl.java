package ru.yandex.practicum.shoppingstore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.interactionapi.enums.ProductCategory;
import ru.yandex.practicum.interactionapi.enums.ProductState;
import ru.yandex.practicum.interactionapi.model.PageableDto;
import ru.yandex.practicum.interactionapi.model.ProductDto;
import ru.yandex.practicum.interactionapi.model.ProductQuantityStateRequest;
import ru.yandex.practicum.interactionapi.model.ProductsPageDto;
import ru.yandex.practicum.shoppingstore.dal.ProductMapper;
import ru.yandex.practicum.shoppingstore.exception.NotFoundException;
import ru.yandex.practicum.shoppingstore.model.Product;

import ru.yandex.practicum.shoppingstore.repository.ShoppingStoreRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductsPageDto getProducts(ProductCategory productCategory, PageableDto pageableDto) {
        List<Product> products;
        Pageable pageable = null;
        if (pageableDto.getSort() != null) {
            String sortType = "ASC";
            List<String> sorts = pageableDto.getSort();
            if (sorts.remove("DESC")) {
                sortType = "DESC";
            }
            if (sorts.remove("ASC")) {
                sortType = "ASC";
            }


            pageable = PageRequest.of(pageableDto.getPage(), pageableDto.getSize(), Sort.by(Sort.Direction.valueOf(sortType), (String.join(",", sorts))));


            products = shoppingStoreRepository.findAllByProductCategory(productCategory, pageable);
            if (products.isEmpty()) {
                log.info("возврщен пустой список поиска по категории " + productCategory);
                return new ProductsPageDto();
            } else {
                log.info("возврщен список поиска по категории " + productCategory);

                return new ProductsPageDto(productMapper.productsToProductsDto(products), pageable.getSort().toList());
            }

        } else {
            products = shoppingStoreRepository.findAllByProductCategory(productCategory);

            if (products.isEmpty()) {
                log.info("возврщен пустой список поиска по категории " + productCategory);
                return new ProductsPageDto();
            } else {
                log.info("возврщен список поиска по категории " + productCategory);
                return new ProductsPageDto(productMapper.productsToProductsDto(products), null);
            }
        }
    }

    @Transactional
    @Override
    public ProductDto createProduct(ProductDto productDto) {
        //добавили продукт
        Product product = productMapper.productDtoToProduct(productDto);
        log.info("Добавили приодукт {}", productDto);
        return productMapper.productToProductDto(shoppingStoreRepository.save(product));
    }

    @Transactional
    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = shoppingStoreRepository.findById(productDto.getProductId())
                .orElseThrow(() -> new NotFoundException("не найден товар с id: " + productDto.getProductId()));
        Product newProduct = productMapper.productDtoToProduct(productDto);
        newProduct.setProductId(productDto.getProductId());
        log.info("обновляем продукт id: " + newProduct.getProductId());
        return productMapper.productToProductDto(shoppingStoreRepository.save(newProduct));
    }

    @Transactional
    @Override
    public boolean removeProduct(UUID productId) {
        Product product = shoppingStoreRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("не найден товар с id: " + productId));
        product.setProductState(ProductState.DEACTIVATE);
        log.info("декактивация продукта по id " + productId);
        shoppingStoreRepository.save(product);
        return true;
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        Product product = shoppingStoreRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("не найден товар с id: " + productId));
        log.info("поиск продукта по id " + productId);
        return productMapper.productToProductDto(product);
    }

    @Override
    @Transactional
    public boolean setProductQuantityState(ProductQuantityStateRequest productQuantityStateRequest) {
        Optional<Product> product = shoppingStoreRepository.findByProductId(productQuantityStateRequest.getProductId());
        if (product.isEmpty()) {
            return false;
        } else {
            product.get().setQuantityState(productQuantityStateRequest.getQuantityState());
            shoppingStoreRepository.save(product.get());
            return true;
        }
    }
}
