package ru.yandex.practicum.service;

import org.springframework.data.domain.Pageable;
import ru.yandex.practicum.ProductCategory;
import ru.yandex.practicum.ProductDto;
import ru.yandex.practicum.request.SetProductQuantityStateRequest;

import java.util.List;

public interface ShoppingStoreService {
    List<ProductDto> getProducts(ProductCategory category,
                                 Pageable pageRequest);

    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);

    boolean deleteProduct(String productId);

    boolean setQuantityState(SetProductQuantityStateRequest setProductQuantityStateRequest);

    ProductDto getProduct(String productId);
}
