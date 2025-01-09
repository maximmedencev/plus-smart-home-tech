package ru.yandex.practicum.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError {
    private Throwable cause;
    private StackTraceElement[] stackTrace;
    private HttpStatus httpStatus;
    private String userMessage;
    private String message;
    private Throwable[] suppressed;
    private String localizedMessage;

    public ApiError(HttpStatus httpStatus, BaseException e) {
        this.cause = e.getCause();
        this.stackTrace = e.getStackTrace();
        this.httpStatus = httpStatus;
        this.userMessage = e.getUserMessage();
        this.message = e.getMessage();
        this.suppressed = e.getSuppressed();
        this.localizedMessage = this.getLocalizedMessage();

    }
//
//    public ApiError(HttpStatus httpStatus, NoProductsInShoppingCartException e) {
//        this.cause = e.getCause();
//        this.stackTrace = e.getStackTrace();
//        this.httpStatus = httpStatus;
//        this.userMessage = e.getUserMessage();
//        this.message = e.getMessage();
//        this.suppressed = e.getSuppressed();
//        this.localizedMessage = this.getLocalizedMessage();
//
//    }
//
//    public ApiError(HttpStatus httpStatus, NotEnoughProductsInWarehouseException e) {
//        this.cause = e.getCause();
//        this.stackTrace = e.getStackTrace();
//        this.httpStatus = httpStatus;
//        this.userMessage = e.getUserMessage();
//        this.message = e.getMessage();
//        this.suppressed = e.getSuppressed();
//        this.localizedMessage = this.getLocalizedMessage();
//
//    }
//
//    public ApiError(HttpStatus httpStatus, ProductInShoppingCartNotInWarehouse e) {
//        this.cause = e.getCause();
//        this.stackTrace = e.getStackTrace();
//        this.httpStatus = httpStatus;
//        this.userMessage = e.getUserMessage();
//        this.message = e.getMessage();
//        this.suppressed = e.getSuppressed();
//        this.localizedMessage = this.getLocalizedMessage();
//
//    }
//
//    public ApiError(HttpStatus httpStatus, ProductNotFoundException e) {
//        this.cause = e.getCause();
//        this.stackTrace = e.getStackTrace();
//        this.httpStatus = httpStatus;
//        this.userMessage = e.getUserMessage();
//        this.message = e.getMessage();
//        this.suppressed = e.getSuppressed();
//        this.localizedMessage = this.getLocalizedMessage();
//
//    }
//
//    public ApiError(HttpStatus httpStatus, ProductInShoppingCartLowQuantityInWarehouse e) {
//        this.cause = e.getCause();
//        this.stackTrace = e.getStackTrace();
//        this.httpStatus = httpStatus;
//        this.userMessage = e.getUserMessage();
//        this.message = e.getMessage();
//        this.suppressed = e.getSuppressed();
//        this.localizedMessage = this.getLocalizedMessage();
//
//    }
//
//    public ApiError(HttpStatus httpStatus, NoSpecifiedProductInWarehouseException e) {
//        this.cause = e.getCause();
//        this.stackTrace = e.getStackTrace();
//        this.httpStatus = httpStatus;
//        this.userMessage = e.getUserMessage();
//        this.message = e.getMessage();
//        this.suppressed = e.getSuppressed();
//        this.localizedMessage = this.getLocalizedMessage();
//
//    }
//
//    public ApiError(HttpStatus httpStatus, UnknownServerException e) {
//        this.cause = e.getCause();
//        this.stackTrace = e.getStackTrace();
//        this.httpStatus = httpStatus;
//        this.userMessage = e.getUserMessage();
//        this.message = e.getMessage();
//        this.suppressed = e.getSuppressed();
//        this.localizedMessage = this.getLocalizedMessage();
//
//    }
}