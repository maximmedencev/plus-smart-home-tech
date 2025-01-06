package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.BookedProductsDto;
import ru.yandex.practicum.ShoppingCartDto;
import ru.yandex.practicum.entity.Position;
import ru.yandex.practicum.entity.ShoppingCart;
import ru.yandex.practicum.exception.NoProductsInShoppingCartException;
import ru.yandex.practicum.exception.NotAuthorizedUserException;
import ru.yandex.practicum.mappers.ShoppingCartMappers;
import ru.yandex.practicum.repository.PositionRepository;
import ru.yandex.practicum.repository.ShoppingCartRepository;
import ru.yandex.practicum.request.ChangeProductQuantityRequest;
import ru.yandex.practicum.response.ChangeProductQuantityResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final PositionRepository positionRepository;

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
        }

        return ShoppingCartMappers
                .mapToShoppingCartDto(shoppingCart);
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
        }
        positionRepository.saveAll(positions);

        return new ShoppingCartDto(shoppingCart.getCartId(), namesAndQuantities);
    }

    @Override
    public void deactivateCart(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        shoppingCartRepository.setShoppingCartActive(false, username);
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
        positionRepository.saveAll(positions);

        shoppingCart = shoppingCartRepository
                .getShoppingCartByUsernameAndActive(username, true);

        return new ShoppingCartDto(shoppingCart.getCartId(), positionsListToMap(positions));
    }

    @Override
    public ChangeProductQuantityResponse changeQuantity(String username, ChangeProductQuantityRequest changeProductQuantityRequest) {
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

        return new ChangeProductQuantityResponse(changeProductQuantityRequest.getProductId(),
                changeProductQuantityRequest.getNewQuantity());
    }

    @Override
    public BookedProductsDto booking(String username) {
        if (username.isBlank()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым");
        }
        return null;
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
