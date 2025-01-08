package ru.yandex.practicum.mapper;

import ru.yandex.practicum.dto.ProductCategory;
import ru.yandex.practicum.dto.ProductDto;
import ru.yandex.practicum.dto.ProductState;
import ru.yandex.practicum.dto.QuantityState;
import ru.yandex.practicum.entity.Product;

public class ShoppingStoreMappers {
    public static ProductDto mapToProductDto(Product product) {
        return new ProductDto(product.getProductId(),
                product.getProductName(),
                product.getDescription(),
                product.getImageSrc(),
                QuantityState.valueOf(product.getQuantityState()),
                ProductState.valueOf(product.getProductState()),
                product.getRating(),
                ProductCategory.valueOf(product.getProductCategory()),
                product.getPrice());
    }

    public static Product mapToProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setImageSrc(productDto.getImageSrc());
        product.setQuantityState(productDto.getQuantityState().toString());
        product.setProductState(productDto.getProductState().toString());
        product.setRating(productDto.getRating());
        product.setProductCategory(productDto.getProductCategory().toString());
        product.setPrice(productDto.getPrice());
        return product;
    }
}
