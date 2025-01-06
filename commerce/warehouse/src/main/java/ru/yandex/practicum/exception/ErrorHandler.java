package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ProductInShoppingCartLowQuantityInWarehouse.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError lowQuantity(ProductInShoppingCartLowQuantityInWarehouse exception) {
        log.warn("Статус 400 -  {}", exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST, exception);
    }

    @ExceptionHandler({NoSpecifiedProductInWarehouseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError noSpecifiedProduct(NoSpecifiedProductInWarehouseException exception) {
        log.warn("Статус 400 -  {}", exception.getMessage(), exception);
        return new ApiError(HttpStatus.BAD_REQUEST, exception);
    }


}