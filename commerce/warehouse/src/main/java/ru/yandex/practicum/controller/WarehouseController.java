package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.AddressDto;
import ru.yandex.practicum.BookedProductsDto;
import ru.yandex.practicum.ShoppingCartDto;
import ru.yandex.practicum.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;
import ru.yandex.practicum.service.WarehouseService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/warehouse")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addNewProduct(@Valid @RequestBody NewProductInWarehouseRequest request) {
        warehouseService.addNewProduct(request);
    }

    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto check(@Valid @RequestBody ShoppingCartDto shoppingCartDto) {
        return warehouseService.check(shoppingCartDto);

    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addProduct(@Valid @RequestBody AddProductToWarehouseRequest request) {
        warehouseService.addProduct(request);
    }

    @GetMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public AddressDto getAddress() {
        return warehouseService.getAddress();
    }


}
