package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findByUsername(String username);

    @Transactional
    @Modifying
    @Query("update Order o set o.state = :st where o.orderId = :oid")
    void setOrderStatus(@Param("st") String state, @Param("oid") String orderId);
}