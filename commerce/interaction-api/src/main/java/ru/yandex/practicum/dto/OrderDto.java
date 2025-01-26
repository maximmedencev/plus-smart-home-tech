package ru.yandex.practicum.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Builder
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
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
