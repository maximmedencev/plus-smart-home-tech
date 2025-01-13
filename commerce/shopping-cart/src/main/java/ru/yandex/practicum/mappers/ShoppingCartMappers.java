package ru.yandex.practicum.mappers;

import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.entity.Position;
import ru.yandex.practicum.entity.ShoppingCart;

import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingCartMappers {
    public static ShoppingCartDto mapToShoppingCartDto(ShoppingCart shoppingCart) {
        Map<String, Integer> namesAndQuantities = shoppingCart.getPositions().stream()
                .collect(Collectors.toMap(Position::getProductId, Position::getQuantity));

        return new ShoppingCartDto(shoppingCart.getCartId(), namesAndQuantities);
    }
}
