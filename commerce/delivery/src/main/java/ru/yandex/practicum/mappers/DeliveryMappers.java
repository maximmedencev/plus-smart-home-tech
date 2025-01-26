package ru.yandex.practicum.mappers;

import ru.yandex.practicum.dto.AddressDto;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.DeliveryState;
import ru.yandex.practicum.entity.Address;
import ru.yandex.practicum.entity.Delivery;

public class DeliveryMappers {
    public static Address mapToAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setCountry(addressDto.getCountry());
        address.setCity(addressDto.getCity());
        address.setStreet(addressDto.getStreet());
        address.setHouse(addressDto.getHouse());
        address.setFlat(addressDto.getFlat());
        return address;
    }

    public static AddressDto mapToAddressDto(Address address) {
        return new AddressDto(
                address.getCountry(),
                address.getCity(),
                address.getStreet(),
                address.getHouse(),
                address.getFlat()
        );
    }

    public static Delivery mapToDelivery(DeliveryDto deliveryDto) {
        Delivery delivery = new Delivery();
        delivery.setDeliveryId(deliveryDto.getDeliveryId());
        delivery.setDeliveryState(deliveryDto.getDeliveryState().toString());
        delivery.setOrderId(deliveryDto.getOrderId());
        delivery.setFromAddress(mapToAddress(deliveryDto.getFromAddress()));
        delivery.setToAddress(mapToAddress(deliveryDto.getToAddress()));
        return delivery;
    }

    public static DeliveryDto mapToDeliveryDto(Delivery delivery) {
        DeliveryDto deliveryDto = new DeliveryDto();
        deliveryDto.setDeliveryId(delivery.getDeliveryId());
        deliveryDto.setDeliveryState(DeliveryState.valueOf(delivery.getDeliveryState()));
        deliveryDto.setOrderId(delivery.getOrderId());
        deliveryDto.setFromAddress(mapToAddressDto(delivery.getFromAddress()));
        deliveryDto.setToAddress(mapToAddressDto(delivery.getToAddress()));
        return deliveryDto;
    }

}
