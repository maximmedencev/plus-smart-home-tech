package ru.yandex.practicum.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.BookedProductsDto;
import ru.yandex.practicum.ShoppingCartDto;
import ru.yandex.practicum.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.response.ChangeProductQuantityResponse;
import ru.yandex.practicum.service.ShoppingCartService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shopping-cart")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShoppingCartController {
    final ShoppingCartService shoppingCartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto getCart(@RequestParam String username) {
        return shoppingCartService.getCart(username);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto addToCart(@RequestParam String username,
                                     @RequestBody Map<String, Integer> products) {
        return shoppingCartService.addToCart(username, products);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deactivateCart(@RequestParam String username) {
        shoppingCartService.deactivateCart(username);
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCartDto remove(@RequestParam String username,
                                  @RequestBody Map<String, Integer> products) {
        return shoppingCartService.remove(username, products);
    }

    @PostMapping("/change-quantity")
    @ResponseStatus(HttpStatus.OK)
    public ChangeProductQuantityResponse changeQuantity(@RequestParam String username,
                                                        @RequestBody ChangeProductQuantityRequest changeProductQuantityRequest) {
        return shoppingCartService.changeQuantity(username, changeProductQuantityRequest);
    }

    @PostMapping("/booking")
    @ResponseStatus(HttpStatus.OK)
    public BookedProductsDto booking(@RequestParam String username) {
        return null;
    }
}