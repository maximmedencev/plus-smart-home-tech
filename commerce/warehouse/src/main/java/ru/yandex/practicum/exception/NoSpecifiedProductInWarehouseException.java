package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoSpecifiedProductInWarehouseException extends RuntimeException {
    private String userMessage;

    public NoSpecifiedProductInWarehouseException(String userMessage) {
        super("NoSpecifiedProductInWarehouseException");
        this.setUserMessage(userMessage);
    }
}
