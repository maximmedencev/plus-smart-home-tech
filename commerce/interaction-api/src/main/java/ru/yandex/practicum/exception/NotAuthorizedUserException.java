package ru.yandex.practicum.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotAuthorizedUserException extends BaseException {

    public NotAuthorizedUserException(String userMessage) {
        super("NotAuthorizedUserException");
        this.setUserMessage(userMessage);
    }
}