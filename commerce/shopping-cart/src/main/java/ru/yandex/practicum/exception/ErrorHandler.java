package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({NotAuthorizedUserException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError authorizationException(NotAuthorizedUserException exception) {
        log.warn("Статус 401 -  {}", exception.getMessage(), exception);
        return new ApiError(HttpStatus.UNAUTHORIZED, exception);
    }

    @ExceptionHandler({NoProductsInShoppingCartException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError noProductsInCartException(NoProductsInShoppingCartException exception) {
        log.warn("Статус 400 -  {}", exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST, exception);
    }

}