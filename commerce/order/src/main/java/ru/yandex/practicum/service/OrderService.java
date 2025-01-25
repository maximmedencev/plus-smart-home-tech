package ru.yandex.practicum.service;

import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;

import java.util.List;

public interface OrderService {

    OrderDto addNewOrder(CreateNewOrderRequest request);

    List<OrderDto> getOrders(String username);

    OrderDto setReturned(ProductReturnRequest request);

    OrderDto setPaid(String orderId);

    OrderDto setPaymentFailed(String orderId);

    OrderDto setDelivered(String orderId);

    OrderDto setDeliveryFailed(String orderId);

    OrderDto setCompleted(String orderId);

    OrderDto calculateTotal(String orderId);

    OrderDto calculateDelivery(String orderId);

    OrderDto setAssembled(String orderId);

    OrderDto setAssemblyFailed(String orderId);
}
