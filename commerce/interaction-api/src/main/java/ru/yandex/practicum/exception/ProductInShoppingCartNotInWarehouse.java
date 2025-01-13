package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductInShoppingCartNotInWarehouse extends BaseException {

    public ProductInShoppingCartNotInWarehouse(String userMessage) {
        super("ProductInShoppingCartNotInWarehouse");
        this.setUserMessage(userMessage);
    }
}
