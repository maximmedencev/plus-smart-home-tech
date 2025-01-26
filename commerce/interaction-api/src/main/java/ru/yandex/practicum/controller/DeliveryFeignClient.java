package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.OrderDto;

@FeignClient(name = "delivery")
public interface DeliveryFeignClient {
    @PostMapping("/api/v1/delivery/cost")
    double cost(@Valid @RequestBody OrderDto orderDto);

}
