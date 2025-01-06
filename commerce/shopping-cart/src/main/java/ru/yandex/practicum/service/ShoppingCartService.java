package ru.yandex.practicum.service;

import ru.yandex.practicum.BookedProductsDto;
import ru.yandex.practicum.ShoppingCartDto;
import ru.yandex.practicum.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.response.ChangeProductQuantityResponse;

import java.util.Map;

public interface ShoppingCartService {
    ShoppingCartDto getCart(String username);

    ShoppingCartDto addToCart(String username,
                              Map<String, Integer> products);

    void deactivateCart(String username);

    ShoppingCartDto remove(String username,
                           Map<String, Integer> products);

    ChangeProductQuantityResponse changeQuantity(String username,
                                                 ChangeProductQuantityRequest changeProductQuantityRequest);


    BookedProductsDto booking(String username);
}
