package ru.yandex.practicum.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.controller.WarehouseFeignClient;
import ru.yandex.practicum.dto.BookedProductsDto;
import ru.yandex.practicum.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.dto.ShoppingCartDto;
import ru.yandex.practicum.entity.Position;
import ru.yandex.practicum.entity.ShoppingCart;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.exception.ProductInShoppingCartNotInWarehouse;
import ru.yandex.practicum.mappers.ShoppingCartMappers;
import ru.yandex.practicum.repository.PositionRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final PositionRepository positionRepository;
    private final WarehouseFeignClient warehouseFeignClient;

    @Override
    public ShoppingCartDto getCart(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }

        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUsernameAndActive(username, true);

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setActive(true);
            shoppingCart.setUsername(username);
            shoppingCart.setPositions(new ArrayList<>());
            shoppingCart = shoppingCartRepository.save(shoppingCart);
            log.debug("Создана корзина {}", shoppingCart);

        }

        ShoppingCartDto shoppingCartDto = ShoppingCartMappers
                .mapToShoppingCartDto(shoppingCart);
        log.info("Возврат {} после запроса корзины", shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto addToCart(String username, Map<String, Integer> products) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }

        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUsernameAndActive(username, true);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setActive(true);
            shoppingCart.setUsername(username);
            shoppingCart.setPositions(new ArrayList<>());
            shoppingCart = shoppingCartRepository.save(shoppingCart);
            log.debug("Создана корзина {}", shoppingCart);

        }

        Map<String, Integer> namesAndQuantities = shoppingCart.getPositions().stream()
                .collect(Collectors.toMap(Position::getProductId, Position::getQuantity));
        namesAndQuantities.putAll(products);

        List<Position> positions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : namesAndQuantities.entrySet()) {
            Position position = new Position();
            Position currentPosition = getPosition(shoppingCart, entry.getKey());
            if (currentPosition != null) {
                position.setId(currentPosition.getId());
            }

            position.setCart(shoppingCart);
            position.setProductId(entry.getKey());
            position.setQuantity(entry.getValue());
            positions.add(position);
            log.debug("Добавлена позиция {} в корзину c id = {}", position, shoppingCart.getCartId());
        }
        positionRepository.saveAll(positions);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(shoppingCart.getCartId(), namesAndQuantities);

        log.info("Возврат {} после добавления товаров", shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public void deactivateCart(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        shoppingCartRepository.setShoppingCartActive(false, username);
        log.info("Деактивирована корзина пользователя {}", username);
    }

    @Override
    public ShoppingCartDto remove(String username, Map<String, Integer> products) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUsernameAndActive(username, true);

        List<Position> positions = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : products.entrySet()) {
            Position position = new Position();
            position.setCart(shoppingCart);
            position.setProductId(entry.getKey());
            Position currentPosition = getPosition(shoppingCart, entry.getKey());
            if (currentPosition == null) {
                throw new NoProductsInShoppingCartException("Товар " + entry.getKey() + " отсутствует в корзине");
            }
            if (entry.getValue() > currentPosition.getQuantity()) {
                throw new NoProductsInShoppingCartException("Превышено возможное количество единиц " +
                        "товара для удаления для "
                        + entry.getKey());
            }
            position.setQuantity(currentPosition.getQuantity() - entry.getValue());
            positions.add(position);
        }

        for (Position position : shoppingCart.getPositions()) {
            if (!products.containsKey(position.getProductId())) {
                Position oldPosition = new Position();
                oldPosition.setProductId(position.getProductId());
                oldPosition.setQuantity(position.getQuantity());
                oldPosition.setCart(shoppingCart);
                positions.add(oldPosition);
            }
        }

        positionRepository.deleteByCartId(shoppingCart.getCartId());
        log.debug("Очищена корзина с id = {}", shoppingCart.getCartId());
        positionRepository.saveAll(positions);
        log.debug("В корзину с id = {} Записаны позиции {}", shoppingCart.getCartId(),
                positions);

        shoppingCart = shoppingCartRepository
                .getShoppingCartByUsernameAndActive(username, true);

        ShoppingCartDto shoppingCartDto = new ShoppingCartDto(shoppingCart.getCartId(),
                positionsListToMap(positions));
        log.info("Возврат {} после добавления удаления заданных товаров",
                shoppingCartDto);
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        String cartId = shoppingCartRepository.getShoppingCartIdByUsername(username, true);
        Integer positionsChanged = positionRepository.setPositionQuantity(changeProductQuantityRequest.getNewQuantity(),
                cartId,
                changeProductQuantityRequest.getProductId());
        if (positionsChanged == 0) {
            throw new NoProductsInShoppingCartException("Нет искомых товаров в корзине");
        }

        shoppingCartRepository.getShoppingCartIdByUsername(username, true);

        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUsernameAndActive(username, true);

        ShoppingCartDto shoppingCartDto = ShoppingCartMappers
                .mapToShoppingCartDto(shoppingCart);
        log.info("Возврат {} после изменения количества товара", shoppingCartDto);
        return ShoppingCartMappers
                .mapToShoppingCartDto(shoppingCart);
    }

    @Override
    public BookedProductsDto booking(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        ShoppingCart shoppingCart = shoppingCartRepository
                .getShoppingCartByUsernameAndActive(username, true);

        BookedProductsDto bookedProductsDto;
        try {
            log.info("Проверка доступности товара на складе");
            bookedProductsDto = warehouseFeignClient
                    .check(ShoppingCartMappers
                            .mapToShoppingCartDto(shoppingCart));
            log.info("Данные из склада по корзине с id = {} {}", shoppingCart.getCartId(),
                    bookedProductsDto);

        } catch (FeignException e) {
            String userMessage;
            if (e.contentUTF8().indexOf("ProductInShoppingCartLowQuantityInWarehouse") > 0) {
                int userMessageStart = e.contentUTF8().indexOf("\"userMessage\":");
                int userMessageEnd = e.contentUTF8().indexOf("\"message\":", userMessageStart);
                userMessage = e.contentUTF8().substring(userMessageStart + 15, userMessageEnd - 2);
                throw new ProductInShoppingCartNotInWarehouse(userMessage);

            } else {
                userMessage = "Неизвестная ошибка";
                throw new RuntimeException(userMessage);
            }
        }
        return bookedProductsDto;
    }

    private Position getPosition(ShoppingCart shoppingCart, String productId) {
        Optional<Position> optionalPosition = shoppingCart.getPositions().stream()
                .filter(position -> position.getProductId().equals(productId))
                .findFirst();
        return optionalPosition.orElse(null);
    }

    private Map<String, Integer> positionsListToMap(List<Position> positions) {
        return positions.stream().collect(Collectors.toMap(Position::getProductId,
                Position::getQuantity));
    }

}
