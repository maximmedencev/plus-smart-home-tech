package ru.yandex.practicum.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Entity
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "deliveries")
public class Delivery {
    @Id
    @Column(name = "delivery_id")
    String deliveryId;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_address_id")
    Address fromAddress;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "to_address_id")
    Address toAddress;
    @Column(name = "order_id")
    String orderId;
    @Column(name = "delivery_state")
    String deliveryState;
}
