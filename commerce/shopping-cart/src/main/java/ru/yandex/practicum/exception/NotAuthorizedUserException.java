package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotAuthorizedUserException extends RuntimeException {
    private String userMessage;

    public NotAuthorizedUserException(String userMessage) {
        super(userMessage);
        this.setUserMessage(userMessage);
    }
}