package ru.yandex.practicum.shoppingstore.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.interactionapi.model.ProductDto;
import ru.yandex.practicum.shoppingstore.model.Product;


import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    //преобразуем в дто и обратно через @Mapper

    ProductDto productToProductDto(Product product);

    Product productDtoToProduct(ProductDto productDto);

    List<ProductDto> productsToProductsDto(List<Product> products);
}
