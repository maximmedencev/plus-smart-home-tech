package ru.yandex.practicum.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;

@FeignClient(name = "order")
public interface OrderFeignClient {
    @PostMapping("/api/v1/order/assembly")
    public OrderDto assembly(@RequestBody String orderId);
}
