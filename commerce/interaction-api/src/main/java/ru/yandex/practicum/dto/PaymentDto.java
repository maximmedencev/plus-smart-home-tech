package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PaymentDto {
    String paymentId;
    double totalPayment;
    double deliveryTotal;
    double feeTotal;
}
