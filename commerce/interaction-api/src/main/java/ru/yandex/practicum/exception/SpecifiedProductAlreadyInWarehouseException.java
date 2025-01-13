package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecifiedProductAlreadyInWarehouseException extends BaseException {

    public SpecifiedProductAlreadyInWarehouseException(String userMessage) {
        super("SpecifiedProductAlreadyInWarehouseException");
        this.setUserMessage(userMessage);
    }
}