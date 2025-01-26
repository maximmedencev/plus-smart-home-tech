package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderController implements OrderFeignClient {

    private final OrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderDto> orders(@RequestParam String username) {
        return orderService.getOrders(username);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public OrderDto add(@Valid @RequestBody CreateNewOrderRequest request) {
        return orderService.addNewOrder(request);
    }

    @PostMapping("/return")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto returnProducts(@Valid @RequestBody ProductReturnRequest request) {
        return orderService.setReturned(request);
    }

    @PostMapping("/payment")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto payment(@RequestBody String orderId) {
        return orderService.setPaid(orderId);
    }

    @PostMapping("/payment/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto paymentFailed(@RequestBody String orderId) {
        return orderService.setPaymentFailed(orderId);
    }

    @PostMapping("/payment/order/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto delivery(@RequestBody String orderId) {
        return orderService.setDelivered(orderId);
    }

    @PostMapping("/payment/order/delivery/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto deliveryFailed(@RequestBody String orderId) {
        return orderService.setDeliveryFailed(orderId);
    }

    @PostMapping("/completed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto completed(@RequestBody String orderId) {
        return orderService.setCompleted(orderId);
    }

    @PostMapping("/calculate/total")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateTotal(@RequestBody String orderId) {
        return orderService.calculateTotal(orderId);
    }

    @PostMapping("/calculate/delivery")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto calculateDelivery(@RequestBody String orderId) {
        return orderService.calculateDelivery(orderId);
    }

    @PostMapping("/assembly")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assembly(@RequestBody String orderId) {
        return orderService.setAssembled(orderId);
    }

    @PostMapping("/assembly/failed")
    @ResponseStatus(HttpStatus.OK)
    public OrderDto assemblyFailed(@RequestBody String orderId) {
        return orderService.setAssemblyFailed(orderId);
    }

}