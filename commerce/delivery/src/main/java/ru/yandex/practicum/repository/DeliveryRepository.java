package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    Delivery findByOrderId(String orderId);

    @Transactional
    @Modifying
    @Query("update Delivery d set d.deliveryState = :dst where d.deliveryId = :did")
    void setDeliveryStatus(@Param("dst") String deliveryState, @Param("did") String deliveryId);

}