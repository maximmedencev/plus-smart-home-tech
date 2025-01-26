package ru.yandex.practicum.mappers;

import ru.yandex.practicum.dto.PaymentDto;
import ru.yandex.practicum.entity.Payment;

public class PaymentMappers {
    public static PaymentDto mapToPaymentDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPaymentId(payment.getPaymentId());
        paymentDto.setDeliveryTotal(payment.getDeliveryTotal());
        paymentDto.setTotalPayment(payment.getTotalPayment());
        return paymentDto;
    }

    public static Payment mapToPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setPaymentId(paymentDto.getPaymentId());
        payment.setDeliveryTotal(paymentDto.getDeliveryTotal());
        payment.setTotalPayment(paymentDto.getTotalPayment());
        return payment;
    }
}
