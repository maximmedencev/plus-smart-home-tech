package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnknownServerException extends BaseException {

    public UnknownServerException(String userMessage) {
        super("UnknownServerException");
        this.setUserMessage(userMessage);
    }
}