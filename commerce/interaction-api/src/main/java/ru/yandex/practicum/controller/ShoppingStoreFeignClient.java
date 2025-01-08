package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.QuantityState;

import java.util.List;

@FeignClient(name = "shopping-store")
public interface ShoppingStoreFeignClient {
    @GetMapping
    List<ProductDto> getProducts(@RequestParam ProductCategory category,
                                 @RequestParam int page,
                                 @RequestParam int size,
                                 @RequestParam String sort);

    @PutMapping("/api/v1/shopping-store")
    ProductDto addProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store")
    ProductDto updateProduct(@Valid @RequestBody ProductDto productDto);

    @PostMapping("/api/v1/shopping-store/removeProductFromStore")
    boolean deleteProduct(@RequestBody String productId);

    @PostMapping("/api/v1/shopping-store/quantityState")
    boolean setQuantityState(@RequestParam String productId, @RequestParam QuantityState quantityState);

    @GetMapping("/api/v1/shopping-store/{productId}")
    ProductDto getProduct(@PathVariable String productId);

}
