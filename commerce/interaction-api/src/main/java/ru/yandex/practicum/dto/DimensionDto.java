package ru.yandex.practicum.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class DimensionDto {
    @DecimalMin("1.0")
    private double width;
    @DecimalMin("1.0")
    private double height;
    @DecimalMin("1.0")
    private double depth;
}
