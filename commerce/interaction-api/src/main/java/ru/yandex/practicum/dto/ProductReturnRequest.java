package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class ProductReturnRequest {
    String orderId;
    Map<String, Integer> products;
}
