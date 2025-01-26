package ru.yandex.practicum.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDto {
    String paymentId;
    double totalPayment;
    double deliveryTotal;
    double feeTotal;
}
