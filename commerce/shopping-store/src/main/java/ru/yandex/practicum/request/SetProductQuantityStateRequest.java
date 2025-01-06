package ru.yandex.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.QuantityState;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SetProductQuantityStateRequest {
    String productId;
    QuantityState quantityState;
}
