package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {

    @Transactional
    @Modifying
    @Query("delete from Position pos where pos.cart.id = :cid")
    void deleteByCartId(@Param("cid") String cartId);

    @Transactional
    @Modifying
    @Query("update Position p set p.quantity = :quant where p.cart.cartId = :cid and p.productId = :pid")
    Integer setPositionQuantity(@Param("quant") Integer quantity,
                                @Param("cid") String cartId,
                                @Param("pid") String productId);


}
