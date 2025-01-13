package ru.yandex.practicum.repository;


import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.Product;

import java.util.List;

@Repository
public interface ShoppingStoreRepository extends JpaRepository<Product, String> {
    List<Product> findByProductCategory(String productCategory, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "update Product set quantityState = :qs where productId = :pid ")
    void setQuantityState(@Param("pid") String productId, @Param("qs") String quantityState);

    @Transactional
    @Modifying
    @Query(value = "update Product set productState = :ps where productId = :pid ")
    void setProductState(@Param("pid") String productId, @Param("ps") String productState);

}
