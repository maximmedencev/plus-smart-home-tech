package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.ProductCategory;
import ru.yandex.practicum.ProductDto;
import ru.yandex.practicum.ProductState;
import ru.yandex.practicum.entity.Product;
import ru.yandex.practicum.exception.ProductNotFoundException;
import ru.yandex.practicum.mapper.ShoppingStoreMappers;
import ru.yandex.practicum.repository.ShoppingStoreRepository;
import ru.yandex.practicum.request.SetProductQuantityStateRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceImpl implements ShoppingStoreService {

    private final ShoppingStoreRepository shoppingStoreRepository;

    @Override
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageRequest) {
        return shoppingStoreRepository.findByProductCategory(category.toString(), pageRequest).stream()
                .map(ShoppingStoreMappers::mapToProductDto)
                .toList();
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product returnedProduct = shoppingStoreRepository.save(ShoppingStoreMappers.mapToProduct(productDto));
        return ShoppingStoreMappers.mapToProductDto(returnedProduct);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product returnedProduct = shoppingStoreRepository.save(ShoppingStoreMappers.mapToProduct(productDto));
        return ShoppingStoreMappers.mapToProductDto(returnedProduct);
    }

    @Override
    public boolean deleteProduct(String productId) {
        productId = productId.replace("\"", "");
        if (!shoppingStoreRepository.existsById(productId)) {
            throw new ProductNotFoundException("Продукт с id = " + productId + " не найден");
        }

        try {
            shoppingStoreRepository.setProductState(productId, ProductState.DEACTIVATE.toString());
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public boolean setQuantityState(SetProductQuantityStateRequest setProductQuantityStateRequest) {
        try {
            shoppingStoreRepository.setQuantityState(setProductQuantityStateRequest.getProductId(),
                    setProductQuantityStateRequest.getQuantityState().toString());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public ProductDto getProduct(String productId) {
        Optional<Product> optionalProduct = shoppingStoreRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            throw new ProductNotFoundException("Продукт с id = " + productId + " не найден");
        }
        return ShoppingStoreMappers.mapToProductDto(optionalProduct.get());
    }
}
