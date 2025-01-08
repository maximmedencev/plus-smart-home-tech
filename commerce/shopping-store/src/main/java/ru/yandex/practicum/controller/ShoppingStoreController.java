package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.QuantityState;
import ru.yandex.practicum.request.SetProductQuantityStateRequest;
import ru.yandex.practicum.service.ShoppingStoreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-store")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingStoreController implements ShoppingStoreFeignClient {
    final ShoppingStoreService shoppingStoreService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDto> getProducts(@RequestParam ProductCategory category,
                                        @RequestParam int page,
                                        @RequestParam int size,
                                        @RequestParam String sort) {
        return shoppingStoreService.getProducts(category, PageRequest.of(page,
                size,
                Sort.by(sort).ascending()));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        return shoppingStoreService.addProduct(productDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return shoppingStoreService.updateProduct(productDto);
    }

    @PostMapping("/removeProductFromStore")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteProduct(@RequestBody String productId) {
        return shoppingStoreService.deleteProduct(productId);
    }

    @PostMapping("/quantityState")
    @ResponseStatus(HttpStatus.OK)
    public boolean setQuantityState(@RequestParam String productId, @RequestParam QuantityState quantityState) {
        SetProductQuantityStateRequest setProductQuantityStateRequest = new SetProductQuantityStateRequest(productId,
                quantityState);
        return shoppingStoreService.setQuantityState(setProductQuantityStateRequest);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDto getProduct(@PathVariable String productId) {
        return shoppingStoreService.getProduct(productId);
    }
}