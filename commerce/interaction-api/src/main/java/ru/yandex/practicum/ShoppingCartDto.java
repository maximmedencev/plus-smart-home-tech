package ru.yandex.practicum;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartDto {
    private String shoppingCartId;
    Map<String, Integer> products;

}
