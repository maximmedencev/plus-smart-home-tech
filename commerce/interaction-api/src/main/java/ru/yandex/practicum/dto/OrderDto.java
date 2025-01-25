package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class OrderDto {
    String orderId;
    String shoppingCartId;
    Map<String, Integer> products;
    String paymentId;
    String deliveryId;
    OrderState state;
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
    Double totalPrice;
    Double deliveryPrice;
    Double productPrice;
    String username;

}
