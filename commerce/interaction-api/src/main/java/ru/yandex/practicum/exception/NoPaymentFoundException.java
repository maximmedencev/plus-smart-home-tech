package ru.yandex.practicum.exception;

public class NoPaymentFoundException extends BaseException {

    public NoPaymentFoundException(String userMessage) {
        super("NoPaymentFoundException");
        this.setUserMessage(userMessage);
    }
}