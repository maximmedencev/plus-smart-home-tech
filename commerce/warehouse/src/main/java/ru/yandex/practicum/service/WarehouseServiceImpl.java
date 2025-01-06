package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.AddressDto;
import ru.yandex.practicum.BookedProductsDto;
import ru.yandex.practicum.ShoppingCartDto;
import ru.yandex.practicum.entity.Product;
import ru.yandex.practicum.exception.NoSpecifiedProductInWarehouseException;
import ru.yandex.practicum.exception.ProductInShoppingCartLowQuantityInWarehouse;
import ru.yandex.practicum.exception.SpecifiedProductAlreadyInWarehouseException;
import ru.yandex.practicum.repository.WarehouseRepository;
import ru.yandex.practicum.request.AddProductToWarehouseRequest;
import ru.yandex.practicum.request.NewProductInWarehouseRequest;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    private static final String[] ADDRESSES =
            new String[]{"ADDRESS_1", "ADDRESS_2"};

    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, 1)];

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        if (warehouseRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException("Товар с id = " +
                    request.getProductId() + " уже занесен в БД");
        }
        Product product = new Product();
        product.setProductId(request.getProductId());
        product.setFragile(request.isFragile());
        product.setWidth(request.getDimension().getWidth());
        product.setHeight(request.getDimension().getHeight());
        product.setDepth(request.getDimension().getDepth());
        product.setWeight(request.getWeight());

        warehouseRepository.save(product);
    }

    @Override
    public BookedProductsDto check(ShoppingCartDto shoppingCartDto) {
        List<String> productIds = shoppingCartDto.getProducts().keySet().stream().toList();
        List<Product> products = warehouseRepository.findAllById(productIds);

        Map<String, Product> warehouseProducts = products.stream()
                .collect(Collectors.toMap(Product::getProductId, product -> product));

        for (Map.Entry<String, Integer> entry : shoppingCartDto.getProducts().entrySet()) {
            if (entry.getValue() == 0
                    || warehouseProducts.get(entry.getKey()) == null
                    || entry.getValue() > warehouseProducts.get(entry.getKey()).getQuantity()) {
                throw new ProductInShoppingCartLowQuantityInWarehouse("Товара с id = " +
                        entry.getKey() + " нет в достаточном количестве на складе");
            }
        }

        return new BookedProductsDto(
                warehouseProducts.values().stream()
                        .mapToDouble(Product::getWeight).sum(),
                warehouseProducts.values().stream()
                        .mapToDouble(value -> value.getWidth() * value.getHeight() * value.getDepth()).sum(),
                isFragile(products)
        );
    }

    @Override
    public void addProduct(AddProductToWarehouseRequest request) {
        int rowsUpdated = warehouseRepository.setQuantity(request.getQuantity(), request.getProductId());
        if (rowsUpdated == 0) {
            throw new NoSpecifiedProductInWarehouseException("В БД нет продукта с id = " + request.getProductId());
        }
    }

    @Override
    public AddressDto getAddress() {
        String address = CURRENT_ADDRESS;
        return new AddressDto(address, address, address, address);
    }

    private boolean isFragile(List<Product> products) {
        boolean fragile = false;
        for (Product product : products) {
            if (product.getFragile()) {
                return true;
            }
        }
        return false;
    }
}
