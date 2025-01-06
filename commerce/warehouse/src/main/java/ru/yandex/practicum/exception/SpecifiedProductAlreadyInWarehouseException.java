package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {
    private String userMessage;

    public SpecifiedProductAlreadyInWarehouseException(String userMessage) {
        super(userMessage);
        this.setUserMessage(userMessage);
    }
}