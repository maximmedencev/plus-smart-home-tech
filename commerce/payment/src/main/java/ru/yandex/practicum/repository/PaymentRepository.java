package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    @Transactional
    @Modifying
    @Query("update Payment p set p.paymentStatus = :pst where p.paymentId = :pid")
    void setPaymentStatus(@Param("pst") String paymentStatus, @Param("pid") String paymentId);
}

