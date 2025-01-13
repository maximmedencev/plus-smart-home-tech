package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInShoppingCartLowQuantityInWarehouse extends BaseException {

    public ProductInShoppingCartLowQuantityInWarehouse(String userMessage) {
        super("ProductInShoppingCartLowQuantityInWarehouse");
        this.setUserMessage(userMessage);
    }
}