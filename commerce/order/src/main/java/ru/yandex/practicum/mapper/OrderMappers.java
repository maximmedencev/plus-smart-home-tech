package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.OrderState;
import ru.yandex.practicum.entity.Order;
import ru.yandex.practicum.entity.Position;

import java.util.stream.Collectors;

public class OrderMappers {
    public static OrderDto mapToOrderDto(Order order) {
        return OrderDto.builder().
                orderId(order.getOrderId()).
                shoppingCartId(order.getCartId()).
                paymentId(order.getPaymentId()).
                deliveryId(order.getDeliveryId()).
                state(OrderState.valueOf(order.getState())).
                deliveryWeight(order.getDeliveryWeight()).
                deliveryVolume(order.getDeliveryVolume()).
                fragile(order.isFragile()).
                totalPrice(order.getTotalPrice()).
                deliveryPrice(order.getDeliveryPrice()).
                productPrice(order.getProductPrice()).
                products(order.getPositions().stream().
                        collect(Collectors.toMap(Position::getProductId,
                                Position::getQuantity)))
                .username(order.getUsername())
                .build();
    }

    public static Order mapToOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderId(orderDto.getOrderId());
        order.setCartId(orderDto.getShoppingCartId());
        order.setPaymentId(orderDto.getPaymentId());
        order.setDeliveryId(orderDto.getDeliveryId());
        order.setState(orderDto.getState().toString());
        order.setDeliveryWeight(orderDto.getDeliveryWeight());
        order.setDeliveryVolume(orderDto.getDeliveryVolume());
        order.setFragile(orderDto.getFragile());
        order.setTotalPrice(orderDto.getTotalPrice());
        order.setDeliveryPrice(orderDto.getDeliveryPrice());
        order.setProductPrice(orderDto.getProductPrice());
        order.setUsername(orderDto.getUsername());
        return order;
    }

}
