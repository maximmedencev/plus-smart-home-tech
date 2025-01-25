package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoDeliveryFoundException extends BaseException {

    public NoDeliveryFoundException(String userMessage) {
        super("NoDeliveryFoundException");
        this.setUserMessage(userMessage);
    }
}