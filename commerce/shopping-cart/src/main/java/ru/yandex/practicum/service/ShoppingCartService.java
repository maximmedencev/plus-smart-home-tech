package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String username);

    ShoppingCartDto addToCart(String username,
                              Map<String, Integer> products);

    void deactivateCart(String username);

    ShoppingCartDto remove(String username,
                           Map<String, Integer> products);

    ShoppingCartDto changeQuantity(String username,
                                   ChangeProductQuantityRequest changeProductQuantityRequest);


    BookedProductsDto booking(String username);
}
