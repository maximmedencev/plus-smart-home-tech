package ru.yandex.practicum.exception;

public class NoOrderFoundException extends BaseException {

    public NoOrderFoundException(String userMessage) {
        super("NoOrderFoundException");
        this.setUserMessage(userMessage);
    }
}