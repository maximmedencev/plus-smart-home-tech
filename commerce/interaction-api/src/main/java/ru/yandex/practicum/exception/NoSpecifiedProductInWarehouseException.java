package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoSpecifiedProductInWarehouseException extends BaseException {

    public NoSpecifiedProductInWarehouseException(String userMessage) {
        super("NoSpecifiedProductInWarehouseException");
        this.setUserMessage(userMessage);
    }
}
