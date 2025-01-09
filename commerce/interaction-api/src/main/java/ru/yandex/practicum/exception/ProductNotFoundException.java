package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductNotFoundException extends BaseException {

    public ProductNotFoundException(String userMessage) {
        super(userMessage);
        this.setUserMessage(userMessage);
    }
}