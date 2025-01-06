package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {
    Throwable cause;
    StackTraceElement[] stackTrace;
    HttpStatus httpStatus;
    String userMessage;
    String message;
    Throwable[] suppressed;
    String localizedMessage;

    public ApiError(HttpStatus httpStatus, NotAuthorizedUserException e) {
        this.cause = e.getCause();
        this.stackTrace = e.getStackTrace();
        this.httpStatus = httpStatus;
        this.userMessage = e.getUserMessage();
        this.message = e.getMessage();
        this.suppressed = e.getSuppressed();
        this.localizedMessage = this.getLocalizedMessage();

    }

    public ApiError(HttpStatus httpStatus, NoProductsInShoppingCartException e) {
        this.cause = e.getCause();
        this.stackTrace = e.getStackTrace();
        this.httpStatus = httpStatus;
        this.userMessage = e.getUserMessage();
        this.message = e.getMessage();
        this.suppressed = e.getSuppressed();
        this.localizedMessage = this.getLocalizedMessage();

    }
}