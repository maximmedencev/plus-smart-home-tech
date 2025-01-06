package ru.yandex.practicum.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class AddProductToWarehouseRequest {
    @NotNull
    @NotBlank
    private String productId;
    @Min(1)
    private int quantity;
}
