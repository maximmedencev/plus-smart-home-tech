package ru.yandex.practicum.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

@FeignClient(name = "payment")
public interface PaymentFeignClient {
    @PostMapping("/api/v1/payment")
    PaymentDto payment(OrderDto orderDto);
}