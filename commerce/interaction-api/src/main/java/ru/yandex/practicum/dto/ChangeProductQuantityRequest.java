package ru.yandex.practicum.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ChangeProductQuantityRequest {
    private String productId;
    private int newQuantity;
}
