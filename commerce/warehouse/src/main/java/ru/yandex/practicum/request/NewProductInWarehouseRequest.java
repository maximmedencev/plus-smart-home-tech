package ru.yandex.practicum.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yandex.practicum.DimensionDto;

@Getter
@Setter
@ToString
public class NewProductInWarehouseRequest {
    @NotNull
    @NotBlank
    private String productId;
    private boolean fragile;
    @NotNull
    @NotBlank
    private DimensionDto dimension;
    @DecimalMin("1.0")
    private double weight;

}
