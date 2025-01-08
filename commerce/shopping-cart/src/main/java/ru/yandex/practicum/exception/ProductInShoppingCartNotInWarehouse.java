package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInShoppingCartNotInWarehouse extends RuntimeException {
    private String userMessage;

    public ProductInShoppingCartNotInWarehouse(String userMessage) {
        super("ProductInShoppingCartNotInWarehouse");
        this.setUserMessage(userMessage);
    }
}
