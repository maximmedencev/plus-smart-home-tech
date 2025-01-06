package ru.yandex.practicum;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {
    String productId;
    String productName;
    String description;
    String imageSrc;
    QuantityState quantityState;
    ProductState productState;
    @DecimalMin("1.0")
    @DecimalMax("5.0")
    Double rating;
    ProductCategory productCategory;
    @DecimalMin("1.0")
    Double price;
}
