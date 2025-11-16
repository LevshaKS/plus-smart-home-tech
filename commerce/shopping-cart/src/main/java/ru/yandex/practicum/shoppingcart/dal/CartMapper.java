package ru.yandex.practicum.shoppingcart.dal;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import ru.yandex.practicum.interactionapi.model.ShoppingCartDto;
import ru.yandex.practicum.shoppingcart.model.Cart;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {
    //преобразуем в дто и обратно через @Mapper
    ShoppingCartDto cartToShoppingCartDto(Cart cart);
}
