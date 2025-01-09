package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private String userMessage;

    public BaseException(String userMessage) {
        super(userMessage);
    }
}
