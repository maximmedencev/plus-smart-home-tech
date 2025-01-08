package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInShoppingCartLowQuantityInWarehouse extends RuntimeException {
    private String userMessage;

    public ProductInShoppingCartLowQuantityInWarehouse(String userMessage) {
        super("ProductInShoppingCartLowQuantityInWarehouse");
        this.setUserMessage(userMessage);
    }
}