package ru.yandex.practicum.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "payments")
public class Payment {
    @Id
    @UuidGenerator
    @Column(name = "payment_id")
    String paymentId;
    @Column(name = "total_payment")
    Double totalPayment;
    @Column(name = "delivery_total")
    Double deliveryTotal;
    @Column(name = "product_total")
    Double productTotal;
    @Column(name = "payment_status")
    String paymentStatus;
}
