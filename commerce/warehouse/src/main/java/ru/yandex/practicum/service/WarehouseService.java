package ru.yandex.practicum.service;

import ru.yandex.practicum.AddressDto;
import ru.yandex.practicum.BookedProductsDto;
import ru.yandex.practicum.ShoppingCartDto;
import ru.yandex.practicum.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;

public interface WarehouseService {
    void addNewProduct(NewProductInWarehouseRequest request);

    BookedProductsDto check(ShoppingCartDto shoppingCartDto);

    void addProduct(AddProductToWarehouseRequest request);

    AddressDto getAddress();

}
