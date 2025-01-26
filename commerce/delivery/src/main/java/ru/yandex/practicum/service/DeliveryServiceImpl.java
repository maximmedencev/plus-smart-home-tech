package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.controller.OrderFeignClient;
import ru.yandex.practicum.dto.DeliveryDto;
import ru.yandex.practicum.dto.DeliveryState;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.entity.Address;
import ru.yandex.practicum.entity.Delivery;
import ru.yandex.practicum.exception.NoDeliveryFoundException;
import ru.yandex.practicum.mappers.DeliveryMappers;
import ru.yandex.practicum.repository.AddressRepository;
import ru.yandex.practicum.repository.DeliveryRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final AddressRepository addressRepository;
    private final OrderFeignClient orderFeignClient;
    private static final double BASE_DELIVERY_COST = 5.0;
    private static final String ADDRESS_2 = "ADDRESS_2";

    @Override
    public DeliveryDto create(DeliveryDto deliveryDto) {
        log.info("Запись {} в БД", deliveryDto);
        Delivery delivery = DeliveryMappers.mapToDelivery(deliveryDto);

        Optional<Address> optionalToAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndFlat(
                deliveryDto.getToAddress().getCountry(),
                deliveryDto.getToAddress().getCity(),
                deliveryDto.getToAddress().getStreet(),
                deliveryDto.getToAddress().getHouse(),
                deliveryDto.getToAddress().getFlat()
        );

        optionalToAddress.ifPresent(delivery::setToAddress);

        Optional<Address> optionalFromAddress = addressRepository.findByCountryAndCityAndStreetAndHouseAndFlat(
                deliveryDto.getFromAddress().getCountry(),
                deliveryDto.getFromAddress().getCity(),
                deliveryDto.getFromAddress().getStreet(),
                deliveryDto.getFromAddress().getHouse(),
                deliveryDto.getFromAddress().getFlat()
        );

        optionalFromAddress.ifPresent(delivery::setFromAddress);

        DeliveryDto deliveryDtoToReturn = DeliveryMappers.mapToDeliveryDto(deliveryRepository.save(delivery));
        log.info("Успешное сохоанение {} в БД", delivery);
        return deliveryDtoToReturn;
    }

    @Override
    public void setSuccessful(String deliveryId) {
        log.info("Выставляю статус доставки c id = {} равным DELIVERED", deliveryId);
        if (!deliveryRepository.existsById(deliveryId)) {
            throw new NoDeliveryFoundException(
                    "Не найдена доставка с id = " + deliveryId);
        }
        deliveryRepository.setDeliveryStatus(DeliveryState.DELIVERED.toString(), deliveryId);
    }

    @Override
    public void setPicked(String deliveryId) {
        log.info("Выставляю статус доставки c id = {} равным IN_PROGRESS", deliveryId);
        Optional<Delivery> optionalDelivery = deliveryRepository.findById(deliveryId);
        if (optionalDelivery.isEmpty()) {
            throw new NoDeliveryFoundException(
                    "Не найдена доставка для расчёта для выдачи с id = " + deliveryId);
        }
        deliveryRepository.setDeliveryStatus(DeliveryState.IN_PROGRESS.toString(), deliveryId);
        orderFeignClient.assembly(optionalDelivery.get().getOrderId());
    }

    @Override
    public void setFailed(String deliveryId) {
        log.info("Выставляю статус доставки c id = {} равным FAILED", deliveryId);
        if (!deliveryRepository.existsById(deliveryId)) {
            throw new NoDeliveryFoundException(
                    "Не найдена доставка с id = " + deliveryId);
        }
        deliveryRepository.setDeliveryStatus(DeliveryState.FAILED.toString(), deliveryId);
    }

    @Override
    public double getCost(OrderDto orderDto) {
        log.info("Расчет стоимость доставки заказа {}", orderDto.getOrderId());
        Delivery delivery = deliveryRepository.findByOrderId(orderDto.getOrderId())
                .orElseThrow(() -> new NoDeliveryFoundException(
                        "Не найдена доставка для расчёта для заказа с id = " + orderDto.getOrderId()));
        double cost = BASE_DELIVERY_COST;

        if (addressContainsValue(delivery.getFromAddress(), ADDRESS_2)) {
            cost += cost + cost * 2;
        }
        if (orderDto.getFragile()) {
            cost = cost + cost * 0.2;
        }
        cost = cost + cost * orderDto.getDeliveryWeight() * 0.3;
        cost = cost + cost * orderDto.getDeliveryVolume() * 0.2;

        if (delivery.getFromAddress().getCountry().equals(delivery.getToAddress().getCountry()) &&
                delivery.getFromAddress().getCity().equals(delivery.getToAddress().getCity()) &&
                delivery.getFromAddress().getStreet().equals(delivery.getToAddress().getStreet())) {
            return cost;
        }

        return cost + cost * 0.2;
    }

    private boolean addressContainsValue(Address address, String value) {
        return address.getCountry().equals(value) ||
                address.getCity().equals(value) ||
                address.getStreet().equals(value) ||
                address.getHouse().equals(value) ||
                address.getFlat().equals(value);
    }
}
