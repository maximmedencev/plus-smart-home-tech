package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.controller.DeliveryFeignClient;
import ru.yandex.practicum.controller.WarehouseFeignClient;
import ru.yandex.practicum.dto.CreateNewOrderRequest;
import ru.yandex.practicum.dto.OrderDto;
import ru.yandex.practicum.dto.OrderState;
import ru.yandex.practicum.dto.ProductReturnRequest;
import ru.yandex.practicum.entity.Order;
import ru.yandex.practicum.entity.Position;
import ru.yandex.practicum.exception.NoOrderFoundException;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mapper.OrderMappers;
import ru.yandex.practicum.repository.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final WarehouseFeignClient warehouseFeignClient;
    private final DeliveryFeignClient deliveryFeignClient;

    @Transactional
    @Override
    public OrderDto addNewOrder(CreateNewOrderRequest request) {
        log.info("Создание нового заказа {}", request);
        Order order = new Order();
        order.setCartId(request.getShoppingCart().getShoppingCartId());
        order.setCountry(request.getDeliveryAddress().getCountry());
        order.setCity(request.getDeliveryAddress().getCity());
        order.setStreet(request.getDeliveryAddress().getStreet());
        order.setHouse(request.getDeliveryAddress().getHouse());
        order.setFlat(request.getDeliveryAddress().getFlat());
        order.setState(OrderState.NEW.toString());
        order.setUsername(request.getUsername());

        for (Map.Entry<String, Integer> entry : request.getShoppingCart().
                getProducts().entrySet()) {
            Position position = new Position();
            position.setOrder(order);
            position.setProductId(entry.getKey());
            position.setQuantity(entry.getValue());
            order.getPositions().add(position);
        }

        order = orderRepository.save(order);
        log.info("Создан заказ {}", order);
        return OrderMappers.mapToOrderDto(order);
    }

    @Override
    public List<OrderDto> getOrders(String username) {
        log.info("Получение списка всех заказов пользователя " + username);
        if (username.isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        return orderRepository.findByUsername(username).stream()
                .map(OrderMappers::mapToOrderDto)
                .toList();
    }

    @Override
    public OrderDto setReturned(ProductReturnRequest request) {
        log.info("Произвожу возврат товаров " + request);
        try {
            warehouseFeignClient.returnProducts(request.getProducts());
        } catch (
                FeignException e) {
            String userMessage;
            if (e.contentUTF8().indexOf("NoSpecifiedProductInWarehouseException") > 0) {
                int userMessageStart = e.contentUTF8().indexOf("\"userMessage\":");
                int userMessageEnd = e.contentUTF8().indexOf("\"message\":", userMessageStart);
                userMessage = e.contentUTF8().substring(userMessageStart + 15, userMessageEnd - 2);
                throw new NoSpecifiedProductInWarehouseException(userMessage);
            } else {
                throw new RuntimeException(e.getMessage());
            }
        }

        Optional<Order> optionalOrder = orderRepository.findById(request.getOrderId());
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + request.getOrderId());
        }
        orderRepository.setOrderStatus(OrderState.CANCELED.toString(), request.getOrderId());
        return OrderMappers.mapToOrderDto(optionalOrder.get());
    }

    @Override
    public OrderDto setPaid(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        orderRepository.setOrderStatus(OrderState.PAID.toString(), orderId);
        order.setState(OrderState.PAID.toString());
        log.info("Заказу {} назначен статус PAID", orderId);
        return OrderMappers.mapToOrderDto(order);
    }

    @Override
    public OrderDto setPaymentFailed(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        orderRepository.setOrderStatus(OrderState.PAYMENT_FAILED.toString(), orderId);
        order.setState(OrderState.PAYMENT_FAILED.toString());
        log.info("Заказу {} назначен статус PAYMENT_FAILED", orderId);
        return OrderMappers.mapToOrderDto(order);
    }

    @Override
    public OrderDto setDelivered(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        orderRepository.setOrderStatus(OrderState.DELIVERED.toString(), orderId);
        order.setState(OrderState.DELIVERED.toString());
        log.info("Заказу {} назначен статус DELIVERED", orderId);
        return OrderMappers.mapToOrderDto(order);
    }

    @Override
    public OrderDto setDeliveryFailed(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        orderRepository.setOrderStatus(OrderState.DELIVERY_FAILED.toString(), orderId);
        order.setState(OrderState.DELIVERY_FAILED.toString());
        log.info("Заказу {} назначен статус DELIVERY_FAILED", orderId);
        return OrderMappers.mapToOrderDto(order);
    }

    @Override
    public OrderDto setCompleted(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        orderRepository.setOrderStatus(OrderState.COMPLETED.toString(), orderId);
        order.setState(OrderState.COMPLETED.toString());
        log.info("Заказу {} назначен статус COMPLETED", orderId);
        return OrderMappers.mapToOrderDto(order);
    }

    @Override
    public OrderDto calculateTotal(String orderId) {
        log.info("Произвожу расчет стоимости заказа {}", orderId);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }

        return null;
    }

    @Override
    public OrderDto calculateDelivery(String orderId) {
        log.info("Произвожу расчет стоимости доставки заказа {}", orderId);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        double cost = deliveryFeignClient.cost(OrderMappers.mapToOrderDto(order));
        order.setDeliveryPrice(cost);
        return OrderMappers.mapToOrderDto(orderRepository.save(order));
    }

    @Override
    public OrderDto setAssembled(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        orderRepository.setOrderStatus(OrderState.ASSEMBLED.toString(), orderId);
        order.setState(OrderState.ASSEMBLED.toString());
        log.info("Заказу {} назначен статус ASSEMBLED", orderId);
        return OrderMappers.mapToOrderDto(order);
    }

    @Override
    public OrderDto setAssemblyFailed(String orderId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NoOrderFoundException("Не найден заказ c id = " + orderId);
        }
        Order order = optionalOrder.get();
        orderRepository.setOrderStatus(OrderState.ASSEMBLY_FAILED.toString(), orderId);
        order.setState(OrderState.ASSEMBLY_FAILED.toString());
        log.info("Заказу {} назначен статус ASSEMBLY_FAILED", orderId);
        return OrderMappers.mapToOrderDto(order);
    }

}
