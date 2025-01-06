package ru.yandex.practicum;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookedProductsDto {
    double deliveryWeight;
    double deliveryVolume;
    boolean fragile;

}
