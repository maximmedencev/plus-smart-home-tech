package ru.yandex.practicum.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;

import java.util.Map;

@FeignClient(name = "shopping-cart")
public interface ShoppingCartFeignClient {
    @GetMapping
    ShoppingCartDto getCart(@RequestParam String username);

    @PutMapping("/api/v1/shopping-cart")
    ShoppingCartDto addToCart(@RequestParam String username,
                              @RequestBody Map<String, Integer> products);

    @DeleteMapping("/api/v1/shopping-cart")
    void deactivateCart(@RequestParam String username);

    @PostMapping("/api/v1/shopping-cart/remove")
    ShoppingCartDto remove(@RequestParam String username,
                           @RequestBody Map<String, Integer> products);

    @PostMapping("/api/v1/shopping-cart/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam String username,
                                   @RequestBody ChangeProductQuantityRequest changeProductQuantityRequest);

    @PostMapping("/api/v1/shopping-cart/booking")
    BookedProductsDto booking(@RequestParam String username);
}
