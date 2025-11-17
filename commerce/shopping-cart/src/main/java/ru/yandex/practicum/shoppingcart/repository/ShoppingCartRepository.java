package ru.yandex.practicum.shoppingcart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.shoppingcart.model.Cart;

import java.util.UUID;

//обработка запросов с бд
public interface ShoppingCartRepository extends JpaRepository<Cart, UUID> {

    Cart findByUsername(String username);


}
