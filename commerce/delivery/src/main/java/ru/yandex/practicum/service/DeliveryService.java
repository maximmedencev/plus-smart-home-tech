package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.OrderDto;

public interface DeliveryService {
    DeliveryDto create(DeliveryDto deliveryDto);

    void setSuccessful(String deliveryId);

    void setPicked(String deliveryId);

    void setFailed(String deliveryId);

    double getCost(OrderDto orderDto);
}
