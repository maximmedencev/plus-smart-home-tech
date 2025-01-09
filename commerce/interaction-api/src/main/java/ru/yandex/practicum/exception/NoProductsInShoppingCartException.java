package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoProductsInShoppingCartException extends BaseException {

    public NoProductsInShoppingCartException(String userMessage) {
        super("NoProductsInShoppingCartException");
        this.setUserMessage(userMessage);
    }
}