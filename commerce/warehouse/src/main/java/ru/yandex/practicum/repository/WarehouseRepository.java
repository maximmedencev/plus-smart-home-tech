package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.Product;

@Repository
public interface WarehouseRepository extends JpaRepository<Product, String> {
    @Transactional
    @Modifying
    @Query("update Product p set p.quantity = p.quantity + :quant where p.productId = :pid ")
    Integer setQuantity(@Param("quant") Integer quantity,
                        @Param("pid") String productId);


}
