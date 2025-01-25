package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.service.PaymentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto payment(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.pay(orderDto);
    }

    @PostMapping("/totalCost")
    @ResponseStatus(HttpStatus.OK)
    public double totalCost(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.getTotalCost(orderDto);
    }

    @PostMapping("/refund")
    @ResponseStatus(HttpStatus.OK)
    public void refund(@RequestBody String orderId) {
        paymentService.refund(orderId);
    }

    @PostMapping("/productCost")
    @ResponseStatus(HttpStatus.OK)
    public double productCost(@Valid @RequestBody OrderDto orderDto) {
        return paymentService.getProductsCost(orderDto);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void paymentFailed(@RequestBody String orderId) {
        paymentService.setFailedPayment(orderId);
    }
}
