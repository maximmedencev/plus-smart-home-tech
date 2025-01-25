package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryController implements DeliveryFeignClient {
    private final DeliveryService deliveryService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public DeliveryDto create(@Valid @RequestBody DeliveryDto deliveryDto) {
        return deliveryService.create(deliveryDto);
    }

    @PostMapping("/successful")
    @ResponseStatus(HttpStatus.OK)
    public void successful(@RequestBody String deliveryId) {
        deliveryService.setSuccessful(deliveryId);
    }

    @PostMapping("/picked")
    @ResponseStatus(HttpStatus.OK)
    public void picked(@RequestBody String deliveryId) {
        deliveryService.setPicked(deliveryId);
    }

    @PostMapping("/failed")
    @ResponseStatus(HttpStatus.OK)
    public void failed(@RequestBody String deliveryId) {
        deliveryService.setFailed(deliveryId);
    }

    @PostMapping("/cost")
    @ResponseStatus(HttpStatus.OK)
    public double cost(@Valid @RequestBody OrderDto orderDto) {
        return deliveryService.getCost(orderDto);
    }
}
