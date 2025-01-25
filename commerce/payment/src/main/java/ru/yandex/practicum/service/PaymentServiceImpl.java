package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.controller.ShoppingStoreFeignClient;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.dto.PaymentStatus;
import ru.yandex.practicum.entity.Payment;
import ru.yandex.practicum.exception.NoPaymentFoundException;
import ru.yandex.practicum.exception.NotEnoughInfoInOrderToCalculateException;
import ru.yandex.practicum.mappers.PaymentMappers;
import ru.yandex.practicum.repository.PaymentRepository;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final ShoppingStoreFeignClient shoppingStoreFeignClient;
    private final PaymentRepository paymentRepository;

    @Override
    public PaymentDto pay(OrderDto orderDto) {
        log.info("Подготовливаю и формирую сведения по оплате заказа {}", orderDto.getOrderId());
        Payment payment = new Payment();
        payment.setDeliveryTotal(orderDto.getDeliveryPrice());
        payment.setProductTotal(orderDto.getProductPrice());
        payment.setTotalPayment(orderDto.getTotalPrice());
        payment.setPaymentStatus(PaymentStatus.PENDING.toString());
        return PaymentMappers.mapToPaymentDto(paymentRepository.save(payment));
    }

    @Override
    public double getTotalCost(OrderDto orderDto) {
        log.info("Расчитываю стоимость заказа {}", orderDto.getOrderId());
        if (orderDto.getDeliveryPrice() == null
                || orderDto.getProductPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации в заказе для расчёта");
        }

        return orderDto.getProductPrice()
                + orderDto.getProductPrice() * 0.1
                + orderDto.getDeliveryPrice();
    }

    @Override
    public void refund(String paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new NoPaymentFoundException("Оплата " + paymentId + " не найдена");
        }
        paymentRepository.setPaymentStatus(PaymentStatus.SUCCESS.toString(), paymentId);
    }

    @Override
    public double getProductsCost(OrderDto orderDto) {
        log.info("Произвожу расчет общей стоимости продуктов заказа {}", orderDto.getOrderId());
        double productsCost = 0;
        for (Map.Entry<String, Integer> entry : orderDto.getProducts().entrySet()) {
            double productCost = shoppingStoreFeignClient
                    .getProduct(entry.getKey())
                    .getPrice();
            productsCost += productCost * entry.getValue();
        }
        log.info("Расчитана стоимость заказа {} {}", orderDto.getOrderId(), productsCost);
        return productsCost;
    }

    @Override
    public void setFailedPayment(String paymentId) {
        if (!paymentRepository.existsById(paymentId)) {
            throw new NoPaymentFoundException("Оплата " + paymentId + " не найдена");
        }
        paymentRepository.setPaymentStatus(PaymentStatus.FAILED.toString(), paymentId);
    }
}
