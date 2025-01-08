package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoProductsInShoppingCartException extends RuntimeException {
    private String userMessage;

    public NoProductsInShoppingCartException(String userMessage) {
        super("NoProductsInShoppingCartException");
        this.setUserMessage(userMessage);
    }
}