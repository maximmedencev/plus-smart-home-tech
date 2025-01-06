package ru.yandex.practicum;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AddressDto {
    private String country;
    private String street;
    private String house;
    private String flat;
}
