package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.OrderState;
import ru.yandex.practicum.entity.Order;
import ru.yandex.practicum.entity.Position;

import java.util.stream.Collectors;

public class OrderMappers {
    public static OrderDto mapToOrderDto(Order order) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(order.getOrderId());
        orderDto.setShoppingCartId(order.getCartId());
        orderDto.setPaymentId(order.getPaymentId());
        orderDto.setDeliveryId(order.getDeliveryId());
        orderDto.setState(OrderState.valueOf(order.getState()));
        orderDto.setDeliveryWeight(order.getDeliveryWeight());
        orderDto.setDeliveryVolume(order.getDeliveryVolume());
        orderDto.setFragile(order.isFragile());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setDeliveryPrice(order.getDeliveryPrice());
        orderDto.setProductPrice(order.getProductPrice());
        orderDto.setProducts(order.getPositions().stream().
                collect(Collectors.toMap(Position::getProductId,
                        Position::getQuantity)));
        orderDto.setUsername(order.getUsername());
        return orderDto;
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
