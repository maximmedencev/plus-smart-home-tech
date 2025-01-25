package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.exception.ApiError;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({NotEnoughInfoInOrderToCalculateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError badRequest(NotAuthorizedUserException exception) {
        log.warn("Статус 400 -  {}", exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler({NoPaymentFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFound(NotAuthorizedUserException exception) {
        log.warn("Статус 400 -  {}", exception.getMessage(), exception);
        return new ApiError(HttpStatus.NOT_FOUND, exception);
    }
}