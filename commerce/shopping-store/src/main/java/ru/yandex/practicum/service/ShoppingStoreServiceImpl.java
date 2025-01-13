package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ProductState;
import ru.yandex.practicum.entity.Product;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ShoppingStoreMappers;
import ru.yandex.practicum.repository.ShoppingStoreRepository;
import ru.yandex.practicum.request.SetProductQuantityStateRequest;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;

    @Override
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageRequest) {
        log.info("Запрос продуктов category={}, pageRequest = {}", category, pageRequest);
        List<ProductDto> productDtoList = shoppingStoreRepository.findByProductCategory(category.toString(), pageRequest).stream()
                .map(ShoppingStoreMappers::mapToProductDto)
                .toList();
        log.info("Возвращаю {}", productDtoList);
        return productDtoList;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        log.info("Добавление продукта {}", productDto);
        Product returnedProduct = shoppingStoreRepository.save(ShoppingStoreMappers.mapToProduct(productDto));
        ProductDto productDtoToReturn = ShoppingStoreMappers.mapToProductDto(returnedProduct);
        log.info("Добавлен продукт {}", productDtoToReturn);
        return productDtoToReturn;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        log.info("Обновление данных продукта {}", productDto);
        Product returnedProduct = shoppingStoreRepository.save(ShoppingStoreMappers.mapToProduct(productDto));
        ProductDto productDtoToReturn = ShoppingStoreMappers.mapToProductDto(returnedProduct);
        log.info("Обновлен продукт {}", productDtoToReturn);
        return productDtoToReturn;
    }

    @Override
    public boolean deleteProduct(String productId) {
        productId = productId.replace("\"", ""); // без этого не проходил тест
        if (!shoppingStoreRepository.existsById(productId)) {
            throw new ProductNotFoundException("Продукт с id = " + productId + " не найден");
        }

        try {
            shoppingStoreRepository.setProductState(productId, ProductState.DEACTIVATE.toString());
        } catch (Exception e) {
            log.warn("Неудачное удаление продукта с id = {}", productId);
            return false;
        }
        log.info("Успешно удален продукт с id = {}", productId);
        return true;
    }

    @Override
    public boolean setQuantityState(SetProductQuantityStateRequest setProductQuantityStateRequest) {
        log.info("Запрос на изменение количества товара {}", setProductQuantityStateRequest);
        try {
            shoppingStoreRepository.setQuantityState(setProductQuantityStateRequest.getProductId(),
                    setProductQuantityStateRequest.getQuantityState().toString());
        } catch (Exception e) {
            log.warn("Неудачно изменение количества товара{}", setProductQuantityStateRequest);
            return false;
        }
        log.info("Успешно изменено количество товара {}", setProductQuantityStateRequest);
        return true;
    }

    @Override
    public ProductDto getProduct(String productId) {
        log.info("Запрос информации о товаре с id = {}", productId);
        Optional<Product> optionalProduct = shoppingStoreRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Продукт с id = " + productId + " не найден");
        }
        ProductDto productDto = ShoppingStoreMappers.mapToProductDto(optionalProduct.get());
        log.info("Возврат информации о продукте {}", productDto);
        return productDto;

    }
}
