package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangeProductQuantityRequest {
    String productId;
    int newQuantity;
}
