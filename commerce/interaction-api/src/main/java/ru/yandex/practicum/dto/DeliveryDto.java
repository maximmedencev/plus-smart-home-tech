package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDto {
    String deliveryId;
    AddressDto fromAddress;
    AddressDto toAddress;
    String orderId;
    DeliveryState deliveryState;
}
