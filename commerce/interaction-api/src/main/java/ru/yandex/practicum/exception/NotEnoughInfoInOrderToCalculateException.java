package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotEnoughInfoInOrderToCalculateException extends BaseException {

    public NotEnoughInfoInOrderToCalculateException(String userMessage) {
        super("NotEnoughInfoInOrderToCalculateException");
        this.setUserMessage(userMessage);
    }
}