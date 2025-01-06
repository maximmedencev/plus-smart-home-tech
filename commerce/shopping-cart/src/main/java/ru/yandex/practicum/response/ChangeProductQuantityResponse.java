package ru.yandex.practicum.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ChangeProductQuantityResponse {
    String productId;
    int newQuantity;
}
