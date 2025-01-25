package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;

public interface PaymentService {
    PaymentDto pay(OrderDto orderDto);

    double getTotalCost(OrderDto orderDto);

    void refund(String orderId);

    double getProductsCost(OrderDto orderDto);

    void setFailedPayment(String orderId);
}
